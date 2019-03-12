package com.lightning.jpipeworks.things;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.audioengine.AudioEngine;
import com.lightning.jpipeworks.resources.AudioInputStreamResource;

public class BGMPlayer extends Thing {
    public boolean startMusic = false;
    
    public BGMPlayer(String filename, Engine engine) {
        resources = new ArrayList<>();
        resources.add(new AudioInputStreamResource(this, "assets/" + filename, engine));
        AudioEngine.init();
    }
    
    @Override
    public void update() {
        if(startMusic) {
            try {
                AudioEngine.setBGM(((AudioInputStream) (resources.get(0).resource)));
                startMusic = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void render() {
        
    }
}
