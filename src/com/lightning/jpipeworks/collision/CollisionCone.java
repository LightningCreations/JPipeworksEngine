package com.lightning.jpipeworks.collision;

import com.lightning.jpipeworks.things.PositionedThing;

public class CollisionCone extends Collision {
	private float angleStart; // Where 0 radians = right, pi/2 = up
	private float angleEnd;   // ^
	
	public CollisionCone(PositionedThing anchor, float x, float y, float angleStart, float angleEnd) {
		super(anchor, x, y);
		this.angleStart = angleStart;
		this.angleEnd = angleEnd;
		updateAngles();
	}
	
	public void updateAngles() {
		while(angleStart < 0) angleStart += Math.PI*2;
		while(angleEnd < angleStart) angleEnd += Math.PI*2;
	}

	public boolean collide(Collision b) {
		if(b instanceof CollisionPoint) {
			float angle = (float)Math.atan2(b.getY()-getY(), getX()-b.getX());
			while(angle < angleStart-Math.PI) angle += Math.PI * 2;
			return angle < angleEnd-Math.PI; // Simple, right?
		} else if(b instanceof CollisionMath) {
			return b.collide(this);
		} else return false; // Unimplemented
	}
}
