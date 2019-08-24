package com.lightning.jpipeworks.collision;

import com.lightning.jpipeworks.things.Sprite;

public class CollisionCircle extends Collision {
	private float rSq;
	
	public CollisionCircle(Sprite anchor, float x, float y, float r) {
		super(anchor, x, y);
		this.rSq = r*r;
	}
	
	public boolean collide(Collision b) {
		if(b instanceof CollisionCircle) {
			float thisX = getX();
			float thisY = getY();
			float otherX = b.getX();
			float otherY = b.getY();
			
			float diffX = thisX-otherX;
			float diffY = thisY-otherY;
			
			float distSq = diffX*diffX+diffY*diffY;
			
			return distSq < rSq+((CollisionCircle)b).rSq;
		} else if(b instanceof CollisionMath) {
			// CollisionMath handles this better
			return b.collide(this);
		} else if(b instanceof CollisionPoint) {
			// Same as CollisionCircle, except that the point has an effective radius of 0
			float thisX = getX();
			float thisY = getY();
			float otherX = b.getX();
			float otherY = b.getY();
			
			float diffX = thisX-otherX;
			float diffY = thisY-otherY;
			
			float distSq = diffX*diffX+diffY*diffY;
			
			return distSq < rSq;
		} else return false;
	}
}
