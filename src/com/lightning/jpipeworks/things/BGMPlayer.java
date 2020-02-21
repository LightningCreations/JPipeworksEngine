package com.lightning.jpipeworks.things;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.audioengine.AudioEngine;
import com.lightning.jpipeworks.resources.AudioInputStreamResource;

public class BGMPlayer extends Thing {
    public boolean startMusic = false;
    public boolean loop = true;
    
    public BGMPlayer(String filename, Engine engine) {
        resources = new ArrayList<>();
        resources.add(new AudioInputStreamResource(this, "assets/" + filename, engine));
        AudioEngine.init();
    }
    
    private void startMusic(AudioInputStream in) {
        try {
            AudioEngine.setBGM(in);
            AudioEngine.setBGMLoop(loop);
            startMusic = false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void update() {
        if(startMusic)
            resources.get(0).getResource().map(o->(AudioInputStream)o).ifPresent(this::startMusic);
    }

    @Override
    public void render() {
        
    }
}
