package com.lightning.jpipeworks.resources;

import java.io.File;
import java.util.ArrayList;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.things.Thing;

public class ImageListResource extends Resource {
    public ImageListResource(Thing parent, String pathname, Engine engine) {
        super(parent, pathname, engine);
    }
    
    public ImageListResource(Thing parent, ImageResource[] resources, Engine engine) {
        super(parent, null, engine);
        if(parent != null) {
            for(ImageResource res : resources) {
                parent.resources.add(res);
            }
        }
    }

    @Override
    public void load(String pathname) {
        ArrayList<ImageResource> resource = new ArrayList<>();
        for(int i = 0;; i++) {
            File f = new File(String.format(pathname, i));
            if(f.exists()) {
                synchronized(parent.resources) {
                    ImageResource cur = new ImageResource(parent, String.format(pathname, i), engine);
                    parent.resources.add(cur);
                    resource.add(cur);
                }
            } else break;
        }
        this.resource = resource;
    }

    @Override
    public void free() {
        
    }
    
    @Override
    public void changeParent(Thing newParent) {
        super.changeParent(newParent);
        if(parent != null) {
            for(ImageResource res : (ArrayList<ImageResource>) resource) {
                parent.resources.add(res);
                res.changeParent(parent);
            }
        }
    }
}
