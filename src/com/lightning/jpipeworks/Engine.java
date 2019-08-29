package com.lightning.jpipeworks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.lightning.jpipeworks.Game.GameState;
import com.lightning.jpipeworks.Game.PrimaryGameState;
import com.lightning.jpipeworks.dbg.DebugHub;
import com.lightning.jpipeworks.resources.ImageListResource;
import com.lightning.jpipeworks.resources.ImageResource;
import com.lightning.jpipeworks.resources.Resource;
import com.lightning.jpipeworks.things.Sprite;
import com.lightning.jpipeworks.things.Thing;

public class Engine {
    Game game = null; // not private so PipeworksInternalGame can hack
    private Game realGame;
    
    private GameState loadingState;
    public List<Thing> things = new ArrayList<>();
    public volatile boolean isClosing = false;
    public boolean isLoading = false;
    volatile boolean isRunning = false;
    public boolean[] keysDown = new boolean[65536];
    public static int numLoadThreads = 0; // static in case multiple engines are running
    public static final int MAX_LOAD_THREADS = 4;
    public float delta = 0;
    private AtomicReference<Optional<Function<String,Optional<Supplier<InputStream>>>>> engineResourceLookupFn = new AtomicReference<>(Optional.empty());
    private static final Set<Engine> runningEngines = new HashSet<>();
    //AWT Specific Fields go here
    Window window;
    private BufferedImage image;
    private BufferedImage mainImage;
    private Graphics2D imageGraphics;

    public Engine(Game game) {
        this.game = game;
        this.realGame = game;
    }
    
    /**
     * Sets the engine specific Resource lookup Function. This overrides the default lookup function set with {@link LoadableResource#setLookupFunction}.
     * @param lookupFn The new lookup function. Must be Non-null (Throws NullPointerException if null)
     */
    public void setEngineResourceLookupFunction(Function<String,Optional<Supplier<InputStream>>> lookupFn) {
    	engineResourceLookupFn.set(Optional.of(lookupFn));
    }
    
    public Optional<Function<String,Optional<Supplier<InputStream>>>> getEngineResourceLookupFunction(){
    	return engineResourceLookupFn.get();
    }
    
    public Game getRunningGame() {
    	return this.realGame;//NOTE -> Don't touch, needed so that PipeworksEngineInterface can wrap an existing Engine object.
    }
    
    public static Stream<Engine> getRunningPipeworksEngines(){
    	return runningEngines.stream();
    }
    
    public void close() {
        isClosing = true;
    }
    
    
    public void start() {
    	//TODO Move the following line to the initialization method when that is created
    	runningEngines.add(this);
        JFrame gameFrame = new JFrame("Pipeworks Engine");
        gameFrame.setResizable(false);
        mainImage = new BufferedImage(1024, 576, BufferedImage.TYPE_3BYTE_BGR);
        image = new BufferedImage(1024, 576, BufferedImage.TYPE_3BYTE_BGR);
        imageGraphics = image.createGraphics();
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
        window = gameFrame;
        isLoading = true;
        game = new PipeworksInternalGame(game);
        loadState(game, PrimaryGameState.PIPEWORKS_INTRO);
        long prevTime = System.nanoTime();
        int numFrames = 0;
        double totalSPF = 0;
        isRunning = true;
        while(!isClosing) {
            if(isLoading) {
                boolean allLoaded = true;
                for(Thing thing : things) {
                    if(thing.resources == null) continue;
                    synchronized(thing.resources) { // To allow resources to add other resources
                        for(Resource<?> res : thing.resources) {
                            if(!res.loaded.get()) {
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
            ArrayList<Thing> curThings = new ArrayList<Thing>(things);
            if(!DebugHub.disableUpdate)
	            for(Thing thing : curThings)
	                thing.update();
            if(!DebugHub.disableRender)
	            for(Thing thing : curThings)
	                thing.render();
            DebugHub.update(this);
            mainImage.getGraphics().drawImage(image, 0, 0, null);
            if(!DebugHub.disableClear) {
	            Graphics g = imageGraphics;
	            g.setColor(Color.BLACK);
	            g.fillRect(0, 0, 1024, 576);
            }
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
        isRunning = false;
        gameFrame.setVisible(false);
        window = null;
        runningEngines.remove(this);
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
    
    public void drawLine(int x1,int y1,int x2,int y2,int rgb) {
    	imageGraphics.setColor(new Color(rgb&0xFFFFFF));
    	imageGraphics.drawLine(x1, y1, x2, y2);
    }
    
    public void drawRect(int x1,int y1,int x2,int y2,int rgb) {
    	int len = x2<x1?x1-x2:x2-x1;
    	int height = y2<y1?y1-y2:y2-y1;
    	imageGraphics.setColor(new Color(rgb&0xFFFFFF));
    	imageGraphics.drawRect(x1, y1, len, height);
    }
    
    public void fillRect(int x1,int y1,int x2,int y2,int rgb) {
    	int len = x2<x1?x1-x2:x2-x1;
    	int height = y2<y1?y1-y2:y2-y1;
    	imageGraphics.setColor(new Color(rgb&0xFFFFFF));
    	imageGraphics.fillRect(x1, y1, len, height);
    }
    
    //Internal Helper Method in case we want more interesting shapes
    //Currently just used by drawCircle
    private void draw(Shape s,int rgb) {
    	imageGraphics.setColor(new Color(rgb&0xFFFFFF));
    	imageGraphics.draw(s);
    }
    
    //Internal Helper Method in case we want more interesting shapes
    //Currently just used by fillCircle
    private void fill(Shape s,int rgb) {
    	imageGraphics.setColor(new Color(rgb&0xFFFFFF));
    	imageGraphics.fill(s);
    }
    
    public void drawCircle(int x,int y,int r,int rgb){
    	draw(new Ellipse2D.Double(x, y, r, r),rgb);
    }
    
    public void fillCircle(int x,int y,int r,int rgb) {
    	fill(new Ellipse2D.Double(x, y, r, r),rgb);
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
    
    /**
     * @deprecated Recommended to not use due to lack of compatibility between languages
     * @return the Graphics of the current image
     */
    @Deprecated
    public Graphics getAWTGraphicsObject() {
    	if(this.imageGraphics==null)
    		return this.imageGraphics = image.createGraphics();
    	return imageGraphics;
    }
    
    public int getWidth() {
    	return image.getWidth();
    }
    
    public int getHeight() {
    	return image.getHeight();
    }
    
    public Sprite captureFrame() {
        ImageResource capture = new CapturedImageResource(mainImage, this);
        ImageListResource frames = new ImageListResource(null, new ImageResource[] {capture}, this);
        return new Sprite(frames, new Sprite.EmptyAI(), this);
    }
    
    private static class CapturedImageResource extends ImageResource {
        private CapturedImageResource(BufferedImage image, Engine engine) {
            super(null, image, engine);
        }
    }
}
