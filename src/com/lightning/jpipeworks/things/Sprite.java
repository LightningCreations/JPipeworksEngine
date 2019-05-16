package com.lightning.jpipeworks.things;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.resources.ImageListResource;
import com.lightning.jpipeworks.resources.ImageResource;

public class Sprite extends Thing {
    public static interface SpriteAI {
        public void runAI(Sprite sprite);
    }
    
    private SpriteAI ai;
    public Engine engine;
    public float x, y, width, height;
    public int frame = 0;
    public boolean collideEnable = false;
    public int collisionColor = 0; // Match black
    public boolean collision = false;
    public boolean enable = false;
    private static Camera globalCamera = null;
    
    public static class EmptyAI implements SpriteAI {
        public void runAI(Sprite sprite) {}
    }
    
    public Sprite(String pathname, SpriteAI ai, Engine engine) {
        this(pathname, ai, 0, 0, 256, 256, engine);
    }
    
    public Sprite(ImageListResource res, SpriteAI ai, Engine engine) {
        this(res, ai, 0, 0, 256, 256, engine);
        res.changeParent(this);
    }
    
    public Sprite(String pathname, SpriteAI ai, int x, int y, int width, int height, Engine engine) {
        resources = new ArrayList<>();
        resources.add(new ImageListResource(this, "assets/" + pathname, engine));
        this.ai = ai;
        this.engine = engine;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public Sprite(ImageListResource res, SpriteAI ai, int x, int y, int width, int height, Engine engine) {
        resources = new ArrayList<>();
        resources.add(res);
        this.ai = ai;
        this.engine = engine;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void update() {
        if(enable)
            ai.runAI(this);
    }

    @Override
    public void render() {
        if(enable) {
            if(globalCamera == null) {
                for(Thing t : engine.things) {
                    if(t instanceof Camera) {
                        globalCamera = (Camera) t;
                        break;
                    }
                }
            }
            int offsetX = 0, offsetY = 0;
            if(globalCamera != null) {
                offsetX = globalCamera.offsetX;
                offsetY = globalCamera.offsetY;
            }
            collision = false;
            if(frame < 0) frame = 0;
            if(frame >= resources.size()-1) frame = resources.size()-1;
            if(resources.size() > frame+1) {
                BufferedImage thisImage = (BufferedImage) resources.get(frame+1).resource;
                int trueWidth = thisImage.getWidth();
                int trueHeight = thisImage.getHeight();
                int xOff = (int)(x-width/2)+offsetX;
                int yOff = (int)(y-height/2)+offsetY;
                
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
    }
    
    // This'll be optimized later
    public byte[] toJPEG(int frame) {
        if(frame < 0 || frame >= resources.size()-1) return null;
        
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        BufferedImage thisImage = (BufferedImage) resources.get(frame+1).resource;
        try { ImageIO.write(thisImage, "JPEG", result); } catch (IOException e) { return null; }
        return result.toByteArray();
    }
    
    public byte[][] toJPEGs() {
        byte[][] result = new byte[resources.size()-1][];
        for(int i = 0; i < result.length; i++) {
            result[i] = toJPEG(i);
        }
        return result;
    }
    
    // This'll be optimized later
    public static Sprite fromJPEG(byte[] jpeg, Engine engine) {
        try {
            BufferedImage frame = ImageIO.read(new ByteArrayInputStream(jpeg));
            ImageResource capture = new RecoveredImageResource(frame, engine);
            ImageListResource frames = new ImageListResource(null, new ImageResource[] {capture}, engine);
            return new Sprite(frames, new Sprite.EmptyAI(), engine);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static class RecoveredImageResource extends ImageResource {
        private RecoveredImageResource(BufferedImage image, Engine engine) {
            super(null, image, engine);
        }
    }
}
