package com.lightning.jpipeworks.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Function;

import com.lightning.jpipeworks.Engine;
import com.lightning.jpipeworks.things.Thing;

public abstract class StreamResource<T extends InputStream> extends Resource<T> {
    
    private Optional<T> stream = Optional.empty();
    private final Function<InputStream,Optional<T>> filterStream;
    
    protected StreamResource(Thing parent, String filename, Engine engine,Function<InputStream,Optional<T>> filterStream) {
        super(parent, filename, engine);
        this.filterStream = filterStream;
    }

    @Override
    public void load(String filename) {
        stream = engine
                .getEngineResourceLookupFunction()
                .orElse(LoadableResource.lookupFn.get())
                .andThen(o->o.map(s->s.get()))
                .andThen(o->o.flatMap(filterStream))
                .apply(filename);
        if(!stream.isPresent())
            System.err.printf("Could not load Resource %s%n",filename);
    }
    
    private static void close_unchecked(InputStream in) {
        try {
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void free() {
        stream.ifPresent(StreamResource::close_unchecked);
    }
    
    public final Optional<T> getResource(){
        return stream;
    }

}
