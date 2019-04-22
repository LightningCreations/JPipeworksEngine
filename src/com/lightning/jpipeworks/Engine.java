package com.lightning.jpipeworks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.lightning.jpipeworks.Game.GameState;
import com.lightning.jpipeworks.Game.PrimaryGameState;
import com.lightning.jpipeworks.resources.ImageListResource;
import com.lightning.jpipeworks.resources.ImageResource;
import com.lightning.jpipeworks.resources.Resource;
import com.lightning.jpipeworks.things.Sprite;
import com.lightning.jpipeworks.things.Thing;

public class Engine {
    Game game = null; // not private so PipeworksInternalGame can hack
    private BufferedImage image;
    private GameState loadingState;
    public List<Thing> things = new ArrayList<>();
    public volatile boolean isClosing = false;
    public boolean isLoading = false;
    public boolean[] keysDown = new boolean[65536];
    public static int numLoadThreads = 0; // static in case multiple engines are running
    public static final int MAX_LOAD_THREADS = 4;
    public float delta = 0;
    
    public Engine(Game game) {
        this.game = game;
    }
    
    public void close() {
        isClosing = true;
    }
    
    public void start() {
        JFrame gameFrame = new JFrame("Pipeworks Engine");
        gameFrame.setResizable(false);
        BufferedImage mainImage = new BufferedImage(1024, 576, BufferedImage.TYPE_3BYTE_BGR);
        image = new BufferedImage(1024, 576, BufferedImage.TYPE_3BYTE_BGR);
        ImageIcon icon = new ImageIcon(mainImage);
        JLabel mainLabel = new JLabel(icon);
        gameFrame.add(mainLabel);
        gameFrame.pack();
        gameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gameFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        gameFrame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                keysDown[e.getKeyCode()] = true;
            }
            public void keyReleased(KeyEvent e) {
                keysDown[e.getKeyCode()] = false;
            }
        });
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        isLoading = true;
        game = new PipeworksInternalGame(game);
        loadState(game, PrimaryGameState.PIPEWORKS_INTRO);
        long prevTime = System.nanoTime();
        int numFrames = 0;
        double totalSPF = 0;
        while(!isClosing) {
            if(isLoading) {
                boolean allLoaded = true;
                for(Thing thing : things) {
                    synchronized(thing.resources) { // To allow resources to add other resources
                        for(Resource res : thing.resources) {
                            if(!res.loaded) {
                                allLoaded = false;
                                if(!res.isLoading && numLoadThreads < MAX_LOAD_THREADS) {
                                    numLoadThreads++;
                                    synchronized(res) {
                                        res.notify();
                                    }
                                }
                            }
                        }
                    }
                }
                if(allLoaded) {
                    isLoading = false;
                    game.state = loadingState;
                    game.doneLoading(this, game.state);
                }
            }
            ArrayList<Thing> curThings = (ArrayList<Thing>) ((ArrayList<Thing>) things).clone();
            for(Thing thing : curThings)
                thing.update();
            for(Thing thing : curThings)
                thing.render();
            mainImage.getGraphics().drawImage(image, 0, 0, null);
            Graphics g = image.getGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 1024, 576);
            gameFrame.repaint();
            long curTime;
            while((curTime = System.nanoTime()) < (prevTime + 1000000000/60)) {
                try { Thread.sleep(1); } catch(InterruptedException e) {}
            }
            delta = (curTime-prevTime)/1000000000f;
            totalSPF += delta;
            numFrames++;
            if(numFrames == 60) {
                System.out.printf("FPS: %2.2f\n", 60/totalSPF);
                totalSPF = 0;
                numFrames = 0;
            }
            prevTime = curTime;
        }
        gameFrame.setVisible(false);
    }
    
    public void plotPixel(int x, int y, int r, int g, int b) {
        plotPixel(x, y,
                (r << 16) |
                (g <<  8) |
                (b      ));
    }
    
    public void plotPixel(int x, int y, int rgb) {
        if(x < 0 || x >= 1024 || y < 0 || y >= 576) return;
        image.setRGB(x, y, rgb);
    }
    
    public int getPixel(int x, int y) {
        if(x < 0 || x >= 1024 || y < 0 || y >= 576) return 0; // Black
        return image.getRGB(x, y);
    }
    
    public void loadState(Game game, GameState state) {
        loadingState = state;
        isLoading = true;
        game.loadState(this, state);
    }
    
    @Deprecated
    public Graphics getAWTGraphicsObject() {
    	return image.getGraphics();
    }
    
    public Sprite captureFrame() {
        ImageResource capture = new CapturedImageResource(image, this);
        ImageListResource frames = new ImageListResource(null, new ImageResource[] {capture}, this);
        return new Sprite(frames, new Sprite.EmptyAI(), this);
    }
    
    private static class CapturedImageResource extends ImageResource {
    	
    	/*
    	 * Copy a buffered image instance.
    	 * Answer by Klark from: https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
    	 * 
    	 */
    	private static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null).getSubimage(0, 0, bi.getWidth(), bi.getHeight());
		}
    	
        private CapturedImageResource(BufferedImage image, Engine engine) {
            super(null, deepCopy(image), engine);
        }
    }
}
