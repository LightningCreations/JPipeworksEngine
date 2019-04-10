package com.lightning.jpipeworks;

public abstract class Game {
    protected GameState state;
    
    public static enum GameState {
        PIPEWORKS_LOAD,
        PIPEWORKS_INTRO,
        MAIN_MENU,
        MAIN_GAME
    }
    
    public void loadState(Engine engine, GameState state) {}
    
    public void doneLoading(Engine engine, GameState state) {}
}
