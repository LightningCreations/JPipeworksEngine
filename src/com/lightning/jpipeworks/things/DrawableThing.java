package com.lightning.jpipeworks.things;

public abstract class DrawableThing extends PositionedThing {
	private DrawingSpace space;
	private float x;
	private float y;
	public DrawableThing(DrawingSpace space,float x,float y) {
		this.space = space;
		this.x = x;
		this.y = y;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
	
	public DrawingSpace getDrawingSpace() {
		return space;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}

}
