package com.lightning.jpipeworks.things;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.resources.ImageListResource;

public class Sprite extends Thing {
    public static interface SpriteAI {
        public void runAI(Sprite sprite);
    }
    
    private SpriteAI ai;
    public Engine engine;
    public float x = 0, y = 0, width = 256, height = 256;
    public int frame = 0;
    public boolean collideEnable = false;
    public int collisionColor = 0; // Match black
    public boolean collision = false;
    
    public Sprite(String pathname, SpriteAI ai, Engine engine) {
        resources = new ArrayList<>();
        resources.add(new ImageListResource(this, "assets/" + pathname, engine));
        this.ai = ai;
        this.engine = engine;
    }
    
    public Sprite(String pathname, SpriteAI ai, int x, int y, int width, int height, Engine engine) {
        this(pathname, ai, engine);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void update() {
        ai.runAI(this);
    }

    @Override
    public void render() {
        collision = false;
        BufferedImage thisImage = (BufferedImage) resources.get(frame+1).resource;
        int trueWidth = thisImage.getWidth();
        int trueHeight = thisImage.getHeight();
        int xOff = (int)(x-width/2);
        int yOff = (int)(y-height/2);
        
        for(int curX = 0; curX < width; curX++) {
            for(int curY = 0; curY < height; curY++) {
                int x = curX*trueWidth/(int)width;
                int y = curY*trueHeight/(int)height;
                if(thisImage.getAlphaRaster() != null && thisImage.getRGB(x, y) >= 0) continue;
                if(collideEnable)
                    if((engine.getPixel(curX+xOff, curY+yOff) & 0x00FFFFFF) == collisionColor)
                        collision = true;
                engine.plotPixel(curX+xOff, curY+yOff, thisImage.getRGB(x, y));
            }
        }
    }
}
