package com.lightning.jpipeworks.things;

public abstract class Camera extends PositionedThing {
    public int offsetX;
    public int offsetY;
    
    public Camera() {
    }
    
    public final void render() {
        // Do nothing
    }
    
    public float getX() {
        return -offsetX;
    }
    
    public float getY() {
        return -offsetY;
    }
}
