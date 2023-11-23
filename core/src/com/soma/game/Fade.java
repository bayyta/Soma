package com.soma.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.soma.game.handlers.AssetLoader;

public class Fade {
	
	private OrthographicCamera cam;
	private SpriteBatch sb;
	
	private AssetLoader assetloader;
	private Game game;
	
	private float alpha;
	private boolean back;
	private Game.STATE state;
	private float speed;
	
	public Fade(Game game, AssetLoader assetloader) {
		this.game = game;
		this.assetloader = assetloader;
		cam = new OrthographicCamera();
		cam.setToOrtho(true, Soma.WIDTH, Soma.HEIGHT);
		sb = new SpriteBatch();
		alpha = 0;
		back = false;
		speed = 2;
	}
	
	public void update(float dt) {
		if (!back) alpha += dt * speed;
		else alpha -= dt * speed;
		if (alpha >= 1) {
			alpha = 1;
			back = true;
			Game.currentState = state;
		}
		if (alpha < 0) {
			alpha = 0;
			game.start_fade = false;
			restart();
		}
	}
	
	private void restart() {
		alpha = 0;
		back = false;
	}

	public void render() {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		sb.enableBlending();
		sb.setColor(0, 0, 0, alpha);
		sb.draw(assetloader.menubackground, 0, 0);
		sb.end();
	}
	
	public Game.STATE setState(Game.STATE s) {
		return state = s;
	}

}
