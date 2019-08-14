package com.lightning.jpipeworks;

import java.awt.Container;

import github.lightningcreations.lcjei.IEngineInterface;

class PipeworksEngineInterface implements IEngineInterface<Game> {
	private Game game;
	private Engine engine;
	public PipeworksEngineInterface(Game game, Engine engine) {
		this.game = game;
		this.engine = engine;
	}

	@Override
	public void destroy() throws IllegalStateException {
		if(!engine.isRunning)
			throw new IllegalStateException("Engine is not running");
		engine.close();
	}

	@Override
	public Container getCurrentDrawContainer() {
		// TODO Auto-generated method stub
		return engine.window;
	}

	@Override
	public Game getGameObject() {
		// TODO Auto-generated method stub
		return game;
	}

	@Override
	public void initialize() throws IllegalStateException {
		if(engine.isRunning)
			throw new IllegalStateException("Engine is already running");
		//TODO Figure out how to initialize the engine without actually starting it.
	}

	@Override
	public boolean initialize(Container arg0) throws IllegalStateException {
		initialize();//TODO Rdr I'll let you handle actually implementing this case.
		return false;
	}

	@Override
	public void run() throws IllegalStateException {
		engine.start();
	}

}
