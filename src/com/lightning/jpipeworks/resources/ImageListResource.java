package com.lightning.jpipeworks.resources;

import java.io.File;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.things.Thing;

public class ImageListResource extends Resource {
    public ImageListResource(Thing parent, String pathname, Engine engine) {
        super(parent, pathname, engine);
    }

    @Override
    public void load(String pathname) {
        for(int i = 0;; i++) {
            File f = new File(String.format(pathname, i));
            if(f.exists()) {
                synchronized(parent.resources) {
                    parent.resources.add(new ImageResource(parent, String.format(pathname, i), engine));
                }
            } else break;
        }
    }

    @Override
    public void free() {
        
    }
}
