package com.lightning.jpipeworks.collision;

import com.lightning.jpipeworks.things.Sprite;

public class CollisionPoint extends Collision {
	public CollisionPoint(Sprite anchor, float x, float y) {
		super(anchor, x, y);
	}

	public boolean collide(Collision b) {
		if(b instanceof CollisionCircle) {
			// CollisionCircle handles this better
			return b.collide(this);
		} else if(b instanceof CollisionMath) {
			// CollisionMath handles this better
			return b.collide(this);
		} else if(b instanceof CollisionPoint) {
			float thisX = getX();
			float thisY = getY();
			float otherX = b.getX();
			float otherY = b.getY();
			
			return thisX == otherX && thisY == otherY;
		} else return false;
	}
}
