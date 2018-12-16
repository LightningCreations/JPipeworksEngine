package com.lightning.jpipeworks.things;

import java.util.List;

import com.lightning.jpipeworks.resources.Resource;

public abstract class Thing {
    public List<Resource> resources;

    public abstract void update();
    public abstract void render();
}
