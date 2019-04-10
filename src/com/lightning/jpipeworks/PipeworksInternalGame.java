package com.lightning.jpipeworks;

public class PipeworksInternalGame extends Game {
    private Game realGame;
    
    public PipeworksInternalGame(Game realGame) {
        this.realGame = realGame;
    }
    
    @Override
    public void loadState(Engine engine, GameState state) {
        if(state == GameState.PIPEWORKS_INTRO) {
            engine.loadState(realGame, GameState.MAIN_MENU);
        }
    }
    
    @Override
    public void doneLoading(Engine engine, GameState state) {
        switch(state) {
        case MAIN_MENU:
            engine.game = realGame;
            realGame.doneLoading(engine, GameState.MAIN_MENU);
            break;
        case PIPEWORKS_LOAD:
            engine.loadState(this, GameState.PIPEWORKS_INTRO);
            break;
        default:
            // later
        }
    }
}
