package com.lightning.jpipeworks.collision;

import com.lightning.jpipeworks.things.Sprite;

public abstract class Collision {
	private Sprite anchor;
	private float x;
	private float y;
	public Runnable whenCollided;
	
	public Collision(Sprite anchor, float x, float y) {
		this.anchor = anchor;
		this.x = x;
		this.y = y;
	}
	
	public abstract boolean collide(Collision b);
	
	public float getX() {
		return anchor.x+x;
	}
	
	public float getY() {
		return anchor.y+y;
	}
	
	public Sprite getAnchor() {
		return anchor;
	}
}
