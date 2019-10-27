package com.lightning.jpipeworks.resources;

import java.util.ArrayList;
import java.util.Arrays;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.things.Thing;

public class ImageListResource extends Resource<ArrayList<ImageResource>> {
    public ImageListResource(Thing parent, String pathname, Engine engine) {
        super(parent, pathname, engine);
    }
    
    public ImageListResource(Thing parent, ImageResource[] resources, Engine engine) {
        super(parent, new ArrayList<ImageResource>(Arrays.asList(resources)), engine);
        if(parent != null) {
            for(ImageResource res : resources) {
                parent.resources.add(res);
            }
        }
    }
    
    //FIXME This garbage that isn't aware of LoadableResource.
    @Override
    public void load(String pathname) {
        ArrayList<ImageResource> resource = new ArrayList<>();
        for(int i = 0;; i++) {
        	 synchronized(parent.resources) {
        		 String lpathname = String.format(pathname, i);
                 ImageResource cur = new ImageResource(parent, lpathname, engine);
                 if(!cur.canLoad(lpathname))
                	 break;
                 parent.resources.add(cur);
                 resource.add(cur);
             }
        }
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
