package com.lightning.jpipeworks.resources;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.things.Thing;

public abstract class Resource<T> implements Runnable {
    public final AtomicBoolean loaded = new AtomicBoolean(false);
    public boolean isLoading;
    public boolean error;
    public T resource;//Public fields in a class where subclasses are very likely to have invariants... Very good design
    public Thread loadingThread;
    public String filename;
    protected Thing parent;
    protected Engine engine;
    
    public Resource(Thing parent, String filename, Engine engine) {
        loadingThread = new Thread(this);
        loadingThread.start();
        this.filename = filename;
        this.parent = parent;
        this.engine = engine;
    }
    
    public Resource(Thing parent, T loadedResource, Engine engine) {
        loaded.set(true);;
        resource = loadedResource;
    }
    
    public void run() {
        synchronized(this) {
            try { wait(); } catch(InterruptedException e) {}
        }
        isLoading = true;
        load(filename);
        loaded.set(true);
        Engine.numLoadThreads--;
    }
    
    protected synchronized void queueReload() {
    	if(this.loaded.get()) {
	    	this.loaded.set(false);
	    	this.resource = null;
	    	this.loadingThread = new Thread(this);
	    	this.loadingThread.start();
    	}
    }
    
    public Optional<T> getResource(){
    	if(this.loaded.get())
    		return Optional.ofNullable(resource);
    	else
    		return Optional.empty();
    }
    
    public abstract void load(String filename);
    
    public abstract void free();
    
    public void changeParent(Thing newParent) {
        parent = newParent;
    }
}
