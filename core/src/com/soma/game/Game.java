package com.soma.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.soma.game.handlers.AssetLoader;
import com.soma.game.states.Menu;
import com.soma.game.states.Play;

public class Game {
	
	public enum STATE {
		MENU, PLAY
	}
	public static STATE currentState = STATE.MENU;
	
	private Menu menu;
	private Play play;
	private Fade fade;
	
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	private SpriteBatch sb;
	
	public boolean start_fade = false;
	
	public Game(AssetLoader assetloader, int width, int height) {
		cam = new OrthographicCamera();
		hudCam = new OrthographicCamera();
		sb = new SpriteBatch();
		cam.setToOrtho(true, Soma.WIDTH, Soma.HEIGHT);
		hudCam.setToOrtho(true, Soma.WIDTH, Soma.HEIGHT);
		menu = new Menu(this, assetloader);
		play = new Play(this, assetloader);
		fade = new Fade(this, assetloader);
	}
	
	public OrthographicCamera getCam() {
		return cam;
	}
	
	public OrthographicCamera getHudCam() {
		return hudCam;
	}
	
	public SpriteBatch getSb() {
		return sb;
	}
	
	public void update(float dt) {
		switch(currentState) {
		case MENU:
			menu.update(dt);
			break;
		case PLAY:
			play.update(dt);
			break;
		default:
			break;
		}
		if (start_fade) {
			fade.update(dt);
		}
	}
	
	public void render() {
		switch(currentState) {
		case MENU:
			menu.render();
			break;
		case PLAY:
			play.render();
			break;
		default:
			break;
		}
		if (start_fade) {
			fade.render();
		}
	}
	
	public void startfade(STATE s, String c) {
		start_fade = true;
		if (c == "play") play.restart();
		fade.setState(s);
	}
	
	public void dispose() {
		menu.dispose();
		play.dispose();
	}

}
