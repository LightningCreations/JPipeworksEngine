package com.lightning.jpipeworks.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.things.Thing;

public class ImageResource extends LoadableResource<BufferedImage> {
   
	
	private static Optional<BufferedImage> loadimg(InputStream strm){
		try {
			return Optional.of(ImageIO.read(strm));
		} catch (IOException e) {
			return Optional.empty();
		}
	}
    	
    protected ImageResource(Thing parent, BufferedImage image, Engine engine) {
        super(parent, image, engine);
    }
    
    public ImageResource(Thing parent, String filename, Engine engine) {
        super(parent, filename, engine,ImageResource::loadimg,BufferedImage.class);
    }
    
}
