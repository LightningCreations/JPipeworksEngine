package com.lightning.jpipeworks;

import com.lightning.jpipeworks.audioengine.AudioEngine;
import com.lightning.jpipeworks.things.BGMPlayer;
import com.lightning.jpipeworks.things.Sprite;

public class PipeworksInternalGame extends Game {
    private Game realGame;
    private boolean doneLoading = false;
    private BGMPlayer music;

    private class PipeworksAnimationObject extends Sprite {
        public PipeworksAnimationObject(Engine engine) {
            super("assets/pipeworks/%04d.png", new Sprite.EmptyAI(), engine);
        }
        
        @Override
        public void update() {
            super.update();
            if(AudioEngine.bgm != null && AudioEngine.bgmPos > AudioEngine.bgm.length && doneLoading) {
                engine.game = realGame;
                realGame.doneLoading(engine, PrimaryGameState.MAIN_MENU);
                doneLoading = false;
            }
        }
        
        @Override
        public void render() {
            
        }
    }
    
    public PipeworksInternalGame(Game realGame) {
        this.realGame = realGame;
    }
    
    @Override
    public void loadState(Engine engine, GameState state) {
        if(state == PrimaryGameState.PIPEWORKS_INTRO) {
            engine.things.add(music = new BGMPlayer("music/pipeworksIntro.wav", engine));
            engine.things.add(new PipeworksAnimationObject(engine));
        }
    }
    
    @Override
    public void doneLoading(Engine engine, GameState state) {
        if(!(state instanceof PrimaryGameState)) return;
        switch((PrimaryGameState) state) {
        case MAIN_MENU:
            doneLoading = true;
            break;
        case PIPEWORKS_LOAD:
            engine.loadState(this, PrimaryGameState.PIPEWORKS_INTRO);
            break;
        case PIPEWORKS_INTRO:
            music.loop = false;
            music.startMusic = true;
            engine.loadState(realGame, PrimaryGameState.MAIN_MENU);
            break;
        default:
            // later
        }
    }
}
