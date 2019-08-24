package com.lightning.jpipeworks.collision;

import java.util.ArrayList;

import com.lightning.jpipeworks.things.Thing;

public class CollisionWorld extends Thing {
	private ArrayList<Collision> world;
	
	public CollisionWorld() {
		world = new ArrayList<>();
	}
	
	public void addCollision(Collision collision) {
		world.add(collision);
	}

	@Override
	public void update() {
		for(int ia = 0; ia < world.size(); ia++) {
			Collision a = world.get(ia);
			if(!a.getAnchor().enable) continue;
			for(int ib = ia+1; ib < world.size(); ib++) {
				Collision b = world.get(ib);
				if(!b.getAnchor().enable) continue;
				if(a.collide(b)) {
					a.whenCollided.run();
					b.whenCollided.run();
				}
			}
		}
	}

	@Override
	public void render() {}
}
