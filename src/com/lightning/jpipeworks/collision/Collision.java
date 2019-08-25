package com.lightning.jpipeworks.collision;

import com.lightning.jpipeworks.things.PositionedThing;

public abstract class Collision {
	private PositionedThing anchor;
	private float x;
	private float y;
	public Runnable whenCollided;
	
	public Collision(PositionedThing anchor, float x, float y) {
		this.anchor = anchor;
		this.x = x;
		this.y = y;
	}
	
	public abstract boolean collide(Collision b);
	
	public float getX() {
		if(anchor == null) return x;
		return anchor.getX()+x;
	}
	
	public float getY() {
		if(anchor == null) return y;
		return anchor.getY()+y;
	}
	
	public void setOffsetX(float newX) {
		x = newX;
	}
	
	public void setOffsetY(float newY) {
		y = newY;
	}
	
	public PositionedThing getAnchor() {
		return anchor;
	}
}
