package com.lightning.jpipeworks.things;

public abstract class Camera extends PositionedThing implements DrawingSpace {
    public int offsetX;
    public int offsetY;
    private DrawingSpace origin;
    
    public Camera(DrawingSpace origin) {
    	this.origin = origin;
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

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return origin.getWidth();
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return origin.getHeight();
	}

	@Override
	public void plotPixel(int x, int y, int rgb) {
		origin.plotPixel(x-offsetX, y-offsetY, rgb);
	}

	@Override
	public void drawCircle(int x, int y, int r, int rgb) {
		origin.drawCircle(x-offsetX, y-offsetY, r, rgb);
	}

	@Override
	public void drawRect(int x1, int y1, int x2, int y2, int rgb) {
		origin.drawRect(x1-offsetX, y1-offsetY, x2-offsetX, y2-offsetY, rgb);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2, int rgb) {
		origin.drawLine(x1-offsetX, y1-offsetY, x2-offsetX, y2-offsetY, rgb);
	}

	@Override
	public void fillCircle(int x, int y, int r, int rgb) {
		origin.fillCircle(x-offsetX, y-offsetY, r, rgb);
	}

	@Override
	public void fillRect(int x1, int y1, int x2, int y2, int rgb) {
		origin.fillRect(x1-offsetX, y1-offsetY, x2-offsetX, y2-offsetY, rgb);
	}
	
}
