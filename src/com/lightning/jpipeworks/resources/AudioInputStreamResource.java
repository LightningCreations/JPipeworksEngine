package com.lightning.jpipeworks.resources;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.things.Thing;

public class AudioInputStreamResource extends Resource<AudioInputStream> {
    private static HashMap<String, AudioInputStream> prevLoads = new HashMap<>();
    
    public AudioInputStreamResource(Thing parent, String filename, Engine engine) {
        super(parent, filename, engine);
    }
    
    @Override
    public void load(String filename) {
        if(prevLoads.containsKey(filename)) {
            resource = prevLoads.get(filename);
            return;
        }
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(filename));
            prevLoads.put(filename, inputStream);
            resource = inputStream;
        } catch (IOException | UnsupportedAudioFileException e) {
            error = true;
        }
    }
    
    @Override
    public void free() {
        
    }
}
