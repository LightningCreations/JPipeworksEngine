package com.lightning.jpipeworks.things;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.audioengine.AudioEngine;
import com.lightning.jpipeworks.resources.AudioInputStreamResource;

public class SFXPlayer extends Thing {
    private boolean loaded = false;
    private int id;
    
    public SFXPlayer(String filename, Engine engine) {
        resources = new ArrayList<>();
        resources.add(new AudioInputStreamResource(this, "assets/" + filename, engine));
        AudioEngine.init();
    }
    
    private void load(AudioInputStream in) {
        try {
            id = AudioEngine.addSFX(in);
            loaded = true;
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void update() {
        if(!loaded)
            resources.get(0).getResource().map(o->(AudioInputStream)o).ifPresent(this::load);
    }

    @Override
    public void render() {}
    
    public void play() {
        AudioEngine.playSFX(id);
    }
}
