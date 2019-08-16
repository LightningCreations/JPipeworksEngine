package com.lightning.jpipeworks.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.things.Thing;

public abstract class LoadableResource<T> extends Resource<T> {
	
	public static final class Cached<T>{
		private AtomicReference<T> value;
		private Class<?> valueType;
		private Cached(T value) {
			this.value = new AtomicReference<>(value);
			this.valueType = value.getClass();
		}
		public boolean check(Class<?> cl) {
			return cl.isAssignableFrom(valueType);
		}
		public void invalidate() {
			this.value.set(null);
		}
		public boolean isValid() {
			return value.get()==null;
		}
		public T getValue() {
			return value.get();
		}
	}
	
	private static final Map<String,Cached<?>> cached = Collections.synchronizedMap(new HashMap<>());
	private static final AtomicReference<Function<String,Optional<Supplier<InputStream>>>> lookupFn = new AtomicReference<>(LoadableResource::defaultLookup);
	
	private static InputStream openFile(File f) {
		try {
			return new FileInputStream(f);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private static Optional<Supplier<InputStream>> defaultLookup(String s){
		File f = new File(s);
		if(f.exists())
			return Optional.of(()->openFile(f));
		else
			return Optional.empty();
	}
	
	private volatile Cached<T> value;
	private final Class<?> valueType;
	private final Function<InputStream,Optional<T>> loader;
	
	public synchronized static void setLookupFunction(Function<String,Optional<Supplier<InputStream>>> lookup) {
		lookupFn.set(lookup);
		cached.values().forEach(Cached::invalidate);
		cached.clear();
	}
	
	private synchronized static <T> Cached<T> load(Supplier<InputStream> inSupplier,Function<InputStream,Optional<T>> loader){
		try(InputStream in = inSupplier.get()){
			return loader.andThen(o->o.map(Cached::new).orElse(null)).apply(in);
		} catch (IOException e) {
			return null;
		}
	}
	
	public LoadableResource(Thing parent, String filename, Engine engine,Function<InputStream,Optional<T>> loaderFn,Class<T> cl) {
		super(parent, filename, engine);
		this.valueType = cl;
		this.loader = loaderFn;
	}

	public LoadableResource(Thing parent, T loadedResource, Engine engine) {
		super(parent,loadedResource,engine);
		this.loader = null;
		this.valueType = loadedResource.getClass();
		this.value = new Cached<T>(loadedResource);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void load(String filename) {
		if(cached.containsKey(filename)) {
			Cached<?> c = cached.get(filename);
			if(c!=null&&c.check(valueType)) {
				this.value = (Cached<T>)c;
				this.resource = value.getValue();
			}
		}
		else {
			synchronized(LoadableResource.class) {
				this.value = lookupFn.get().apply(filename).map(s->load(s,loader)).orElse(null);
				if(this.value!=null)
					cached.put(filename, value);
				this.resource = value.getValue();
			}
		}	
	}
	public final boolean canLoad(String filename) {
		return lookupFn.get().apply(filename).isPresent();
	}
	
	public final Optional<T> getResource(){
		if(value==null)
			return Optional.empty();
		else if(!value.isValid()) {
			this.queueReload();
			return Optional.empty();
		}else
			return Optional.ofNullable(value.getValue());
	}

	@Override
	public void free() {
		//Don't need to free anything, but implementations can override if that needs to be changed.
	}

}
