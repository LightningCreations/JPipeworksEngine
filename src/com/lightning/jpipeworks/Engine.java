package com.lightning.jpipeworks;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Engine {
    private Game game = null;
    private HashMap<String, Resource> resources = new HashMap<>();
    public List<Thing> things;
    public boolean isClosing = false;
    
    public Engine(Game game) {
        this.game = game;
        for(Resource r : resources.values()) {
            r.free();
        }
        resources = new HashMap<>();
    }
    
    public void close() {
        isClosing = true;
    }
    
    public void start() {
        JFrame gameFrame = new JFrame("Pipeworks Engine");
        gameFrame.setResizable(false);
        BufferedImage mainImage = new BufferedImage(1280, 720, BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage subImage = new BufferedImage(1280, 720, BufferedImage.TYPE_3BYTE_BGR);
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
        gameFrame.setVisible(true);
        while(!isClosing) {
            
            try { Thread.sleep(10); } catch(InterruptedException e) {}
        }
        gameFrame.setVisible(false);
    }
}
