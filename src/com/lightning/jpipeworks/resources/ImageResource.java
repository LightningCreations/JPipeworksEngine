package com.lightning.jpipeworks.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.things.Thing;

public class ImageResource extends Resource<BufferedImage> {
    private static HashMap<String, BufferedImage> prevLoads = new HashMap<>();
    
    protected ImageResource(Thing parent, BufferedImage image, Engine engine) {
        super(parent, image, engine);
    }
    
    public ImageResource(Thing parent, String filename, Engine engine) {
        super(parent, filename, engine);
    }
    
    @Override
    public void load(String filename) {
        if(prevLoads.containsKey(filename)) {
            resource = prevLoads.get(filename);
            return;
        }
        try {
            BufferedImage image = ImageIO.read(new File(filename));
            prevLoads.put(filename, image);
            resource = image;
        } catch (IOException e) {
            error = true;
        }
    }
    
    @Override
    public void free() {
        
    }
}
