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

    public void update() {
        for(int ia = 0; ia < world.size(); ia++) {
            Collision a = world.get(ia);
            for(int ib = ia+1; ib < world.size(); ib++) {
                Collision b = world.get(ib);
                if(a.collide(b)) {
                    a.whenCollided.run();
                    b.whenCollided.run();
                }
            }
        }
    }
    
    public void collideWith(ArrayList<Collision> others) {
        for(int ia = 0; ia < world.size(); ia++) {
            Collision a = world.get(ia);
            for(int ib = 0; ib < others.size(); ib++) {
                Collision b = others.get(ib);
                if(a.collide(b)) {
                    b.whenCollided.run();
                }
            }
        }
    }
    
    public void collideWith(Collision[] others) {
        for(int ia = 0; ia < world.size(); ia++) {
            Collision a = world.get(ia);
            for(int ib = 0; ib < others.length; ib++) {
                Collision b = others[ib];
                if(a.collide(b)) {
                    b.whenCollided.run();
                }
            }
        }
    }

    public void render() {}
}
