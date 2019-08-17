package com.lightning.jpipeworks.resources;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Function;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.things.Thing;

public abstract class BufferedStreamResource<T extends InputStream> extends StreamResource<T> {

	public BufferedStreamResource(Thing parent, String filename, Engine engine,
			Function<InputStream, Optional<T>> filterStream) {
		super(parent, filename, engine, filterStream.compose(BufferedInputStream::new));
		// TODO Auto-generated constructor stub
	}

}
