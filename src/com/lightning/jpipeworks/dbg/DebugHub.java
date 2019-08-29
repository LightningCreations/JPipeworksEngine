package com.lightning.jpipeworks.dbg;

import java.awt.event.KeyEvent;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.collision.CollisionPoint;
import com.lightning.jpipeworks.collision.CollisionWorld;
import com.lightning.jpipeworks.things.Camera;
import com.lightning.jpipeworks.things.Thing;

public class DebugHub {
	public static boolean disableClear = false;
	public static boolean disableUpdate = false;
	public static boolean disableRender = false;
	
	private static Camera globalCamera = null;
	
	private static boolean hbViewInit = false;
	private static boolean hbViewOn = false;
	private static CollisionPoint[] hbViewColliders;
	private static CollisionWorld hbViewWorld;
	private static int hbViewPos;
	
	private static boolean keyWasDown = false;
	
	public static void initHitboxView(Engine engine) {
		hbViewInit = true;
		// Hook the CollisionWorld object in order to get it to update while updating is disabled
		for(Thing t : engine.things) {
			if(t instanceof CollisionWorld) {
				hbViewWorld = (CollisionWorld) t;
			}
		}
		if(hbViewWorld == null) { hbViewInit = false; return; } // Try again later
		
        if(globalCamera == null) {
            for(Thing t : engine.things) {
                if(t instanceof Camera) {
                    globalCamera = (Camera) t;
                    break;
                }
            }
        }
		
		hbViewColliders = new CollisionPoint[engine.getHeight()];
		
		for(int i = 0; i < hbViewColliders.length; i++) {
			hbViewColliders[i] = new CollisionPoint(globalCamera, 0, i);
			final int curY = i;
			hbViewColliders[i].whenCollided = () -> {
				engine.plotPixel(hbViewPos, curY, 0xFFFFFFFF);
			};
		}
	}
	
	public static void hitboxView(boolean enable, Engine engine) {
		if(!hbViewInit) initHitboxView(engine);
		if(!hbViewInit) return; // Didn't properly initialize, abort!
		if(enable == hbViewOn) return;
		if(enable) {
			disableClear = true;
			disableUpdate = true;
			disableRender = true;
			hbViewPos = 0;
		} else {
			disableClear = false;
			disableUpdate = false;
			disableRender = false;
		}
		hbViewOn = enable;
	}
	
	public static void update(Engine engine) {
		boolean keyDown = false;
        if(engine.keysDown[KeyEvent.VK_H]) {
    		if(!keyWasDown)
    			hitboxView(!hbViewOn, engine);
    		keyDown = true;
        }
        if(hbViewOn) {
        	for(int i = 0; i < hbViewColliders.length; i++) {
        		hbViewColliders[i].setOffsetX(hbViewPos);
        		engine.plotPixel(hbViewPos, i, 0xFFFF0000);
        		if(engine.getPixel(hbViewPos-3, i) != 0xFFFFFFFF) {
        			engine.plotPixel(hbViewPos-3, i, 0);
        		}
        	}
        	hbViewWorld.collideWith(hbViewColliders);
        	hbViewPos++;
        }
        keyWasDown = keyDown;
	}
}
