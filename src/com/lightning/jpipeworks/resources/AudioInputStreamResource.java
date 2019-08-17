package com.lightning.jpipeworks.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.things.Thing;

public class AudioInputStreamResource extends BufferedStreamResource<AudioInputStream> {
	private static Optional<AudioInputStream> loadStream(InputStream in){
		try {
			return Optional.of(AudioSystem.getAudioInputStream(in));
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
    
    public AudioInputStreamResource(Thing parent, String filename, Engine engine) {
        super(parent, filename, engine,AudioInputStreamResource::loadStream);
    }
    
}
