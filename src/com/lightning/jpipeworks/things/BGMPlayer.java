package com.lightning.jpipeworks.things;

import java.util.ArrayList;

import javax.sound.sampled.Clip;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.resources.ClipResource;

public class BGMPlayer extends Thing {
    public boolean startMusic = false;
    
    public BGMPlayer(String filename, Engine engine) {
        resources = new ArrayList<>();
        resources.add(new ClipResource(this, "assets/" + filename, engine));
    }
    
    @Override
    public void update() {
        if(startMusic) {
            Clip clip = (Clip) resources.get(0).resource;
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            startMusic = false;
        }
    }

    @Override
    public void render() {
        
    }
}
