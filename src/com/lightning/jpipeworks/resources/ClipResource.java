package com.lightning.jpipeworks.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.things.Thing;

public class ClipResource extends LoadableResource<Clip> {
    
	private static final Optional<Clip> loadClip(InputStream in){
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(in);
		    DataLine.Info info = new DataLine.Info(Clip.class, inputStream.getFormat());
		    Clip clip = (Clip) AudioSystem.getLine(info);
		    clip.open(inputStream);
		    return Optional.of(clip);
		}catch(IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			return Optional.empty();
		}
	}
    
    public ClipResource(Thing parent, String filename, Engine engine) {
        super(parent, filename, engine,ClipResource::loadClip,Clip.class);
    }
    
  
}
