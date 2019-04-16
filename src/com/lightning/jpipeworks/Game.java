package com.lightning.jpipeworks;

public abstract class Game {
    protected GameState state;
    
    public static enum PrimaryGameState implements GameState {
        MAIN_MENU("Main Menu"),
        MAIN_GAME("Main Game"),
        PIPEWORKS_LOAD("Pipeworks Loading Sequence"),
        PIPEWORKS_INTRO("Pipeworks Intro");
        
        private String name;
        private PrimaryGameState(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public int getID() {
            return hashCode();
        }
    }
    
    public static interface GameState {
        String getName();
        int getID();
    }
    
    public void loadState(Engine engine, GameState state) {}
    
    public void doneLoading(Engine engine, GameState state) {}
}
