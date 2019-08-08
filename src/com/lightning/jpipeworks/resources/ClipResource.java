package com.lightning.jpipeworks.resources;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.things.Thing;

public class ClipResource extends Resource<Clip> {
    private static HashMap<String, Clip> prevLoads = new HashMap<>();
    
    public ClipResource(Thing parent, String filename, Engine engine) {
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
            DataLine.Info info = new DataLine.Info(Clip.class, inputStream.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(AudioSystem.getAudioInputStream(new File(filename)));
            prevLoads.put(filename, clip);
            resource = clip;
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            error = true;
        }
    }
    
    @Override
    public void free() {
        
    }
}
