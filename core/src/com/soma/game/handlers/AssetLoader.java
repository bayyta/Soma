package com.soma.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
	
	public Texture menuspritesheet, gamespritesheet;
	
	public TextureRegion menubackground, smallcircle, bigcircle, middlecircel, rate_menu, moregames;
	public TextureRegion ball, arrow, stick, obstacle, circle, whiteball, death_light, game_over, score, best, home, rate, restart, game_over_circle, new_best;
	
	private static Preferences prefs;
	
	public AssetLoader() {
		load();
	}

	private void load() {
		menuspritesheet = new Texture(Gdx.files.internal("imgs/menuspritesheet.png"));
		menuspritesheet.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gamespritesheet = new Texture(Gdx.files.internal("imgs/gamespritesheet.png"));
		gamespritesheet.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		menubackground = new TextureRegion(menuspritesheet, 0, 0, 720, 1280);
		menubackground.flip(false, true);
		
		smallcircle = new TextureRegion(menuspritesheet, 776, 640, 432, 432);
		smallcircle.flip(false, true);
		
		bigcircle = new TextureRegion(menuspritesheet, 776, 52, 496, 496);
		bigcircle.flip(false, true);
		
		middlecircel = new TextureRegion(menuspritesheet, 1385, 52, 412, 412);
		middlecircel.flip(false, true);
		
		rate_menu = new TextureRegion(menuspritesheet, 1399, 715, 70, 67);
		rate_menu.flip(false, true);
		
		moregames = new TextureRegion(menuspritesheet, 1570, 715, 67, 67);
		moregames.flip(false, true);
		
		ball = new TextureRegion(gamespritesheet, 8, 10, 75, 75);
		ball.flip(false, true);
		
		circle = new TextureRegion(gamespritesheet, 8, 108, 75, 75);
		circle.flip(false, true);
		
		whiteball = new TextureRegion(gamespritesheet, 124, 108, 75, 75);
		whiteball.flip(false, true);
		
		arrow = new TextureRegion(gamespritesheet, 104, 6, 40, 18);
		arrow.flip(false, true);
		
		stick = new TextureRegion(gamespritesheet, 105, 24, 40, 37);
		stick.flip(false, true);
		
		obstacle = new TextureRegion(gamespritesheet, 173, 21, 305, 39);
		obstacle.flip(false, true);
		
		death_light = new TextureRegion(gamespritesheet, 253, 108, 10, 100);
		death_light.flip(false, true);
		
		game_over = new TextureRegion(gamespritesheet, 410, 129, 531, 58);
		game_over.flip(false, true);
		
		score = new TextureRegion(gamespritesheet, 607, 29, 90, 18);
		score.flip(false, true);
		
		best = new TextureRegion(gamespritesheet, 870, 29, 70, 18);
		best.flip(false, true);
		
		home = new TextureRegion(gamespritesheet, 701, 284, 69, 69);
		home.flip(false, true);
		
		rate = new TextureRegion(gamespritesheet, 537, 284, 70, 67);
		rate.flip(false, true);
		
		restart = new TextureRegion(gamespritesheet, 376, 284, 67, 67);
		restart.flip(false, true);
		
		new_best = new TextureRegion(gamespritesheet, 83, 284, 48, 24);
		new_best.flip(false, true);
		
		game_over_circle = new TextureRegion(gamespritesheet, 124, 431, 518, 519);
		game_over_circle.flip(false, true);
		
		// Create (or retrieve existing) preferences file
        prefs = Gdx.app.getPreferences("Soma");

        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }
        prefs.putInteger("highScore", 0);
	}
	
    public void setHighScore(int value) {
        prefs.putInteger("highScore", value);
        prefs.flush();
    }

    public int getHighScore() {
        return prefs.getInteger("highScore");
    }
	
	public void dispose() {
		menuspritesheet.dispose();
		gamespritesheet.dispose();
	}
}
