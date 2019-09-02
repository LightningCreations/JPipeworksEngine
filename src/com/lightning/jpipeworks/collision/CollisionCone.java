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
		} else if(b instanceof CollisionCircle) {
			float dx = getX()-b.getX(), dy = b.getY()-getY();
			
			float angle = (float)Math.atan2(dy, dx);
			while(angle < angleStart-Math.PI) angle += Math.PI * 2;
			if(angle < angleEnd-Math.PI)
				return true; // Simple, right?
			else {
				{
					double radSq = ((CollisionCircle) b).getRadiusSquared();
					double xsina = dx*Math.sin(angleStart);
					double ycosa = dy*Math.cos(angleStart);
					double diff = -xsina - ycosa;
					double discrim = xsina*xsina+xsina*ycosa+ycosa*ycosa-4*(radSq+dx*dx+dy*dy);
					if(discrim<0)
						return false; //No Real Solutions, therefore no collision
					else if(discrim==0&&diff>0)
						return true;
					else if(Math.sqrt(discrim)+diff>0)
						return true;
				}
				{
					double radSq = ((CollisionCircle) b).getRadiusSquared();
					double xsina = dx*Math.sin(angleEnd);
					double ycosa = dy*Math.cos(angleEnd);
					double diff = -xsina - ycosa;
					double discrim = xsina*xsina+xsina*ycosa+ycosa*ycosa-4*(radSq+dx*dx+dy*dy);
					if(discrim<0)
						return false; //No Real Solutions, therefore no collision
					else if(discrim==0&&diff>0)
						return true;
					else
						return Math.sqrt(discrim)+diff>0;
				}
			}
		} else return false; // Unimplemented
	}
}
