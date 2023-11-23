package com.soma.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.soma.game.Game;
import com.soma.game.Soma;
import com.soma.game.handlers.AssetLoader;
import com.soma.game.handlers.InputHandler;

public class Menu {

	private OrthographicCamera cam;
	private SpriteBatch sb;

	private AssetLoader assetloader;
	private Game game;

	private float rot_slow = 0;
	private float rot_fast = 0;

	private boolean expand = false;
	private boolean play = false;

	private Vector3 mouse = new Vector3();

	public Menu(Game game, AssetLoader assetloader) {
		cam = new OrthographicCamera();
		cam.setToOrtho(true, Soma.WIDTH, Soma.HEIGHT);
		sb = new SpriteBatch();
		this.assetloader = assetloader;
		this.game = game;
	}

	public void restart() {
		rot_slow = 0;
		rot_fast = 0;
		expand = false;
		play = false;
	}

	private void handleInput() {
		expand = false;
		if (Gdx.input.justTouched()) {
			mouse.x = Gdx.input.getX();
			mouse.y = Gdx.input.getY();
			cam.unproject(mouse);
			if (mouse.y >= 1196 && mouse.y <= 1196 + assetloader.rate_menu.getRegionHeight()) {
				if (mouse.x >= 254 && mouse.x <= 254 + assetloader.rate_menu.getRegionWidth()) {
					Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.soma.game.android");
				} else if (mouse.x >= 396 && mouse.x <= 396 + assetloader.moregames.getRegionWidth()) {
					Gdx.net.openURI("https://play.google.com/store/apps/developer?id=JWS");
				}
			}
		}
		if (InputHandler.touchDown) {
			mouse.x = Gdx.input.getX();
			mouse.y = Gdx.input.getY();
			cam.unproject(mouse);
			if (mouse.x >= 112 && mouse.x <= (112 + assetloader.bigcircle.getRegionWidth()) && mouse.y >= 392 && mouse.y <= (392 + assetloader.bigcircle.getRegionHeight())) {
				expand = true;
				play = true;
			}
			return;
		}
		if (play) {
			mouse.x = InputHandler.coordinates.x;
			mouse.y = InputHandler.coordinates.y;
			cam.unproject(mouse);
			if (mouse.x >= 112 && mouse.x <= (112 + assetloader.bigcircle.getRegionWidth()) && mouse.y >= 392 && mouse.y <= (392 + assetloader.bigcircle.getRegionHeight())) {
				play = false;
				game.startfade(Game.STATE.PLAY, "play");
			}
		}
	}

	public void update(float dt) {
		handleInput();
		rot_slow -= dt * 9;
		rot_fast += dt * 28;
		if (rot_slow >= 36000) rot_slow = 0;
		if (rot_fast >= 36000) rot_fast = 0;
	}

	public void render() {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		sb.disableBlending();
		sb.draw(assetloader.menubackground, 0, 0);
		sb.enableBlending();
		if (!expand) {
			sb.draw(assetloader.middlecircel, 154, 434);
			sb.draw(assetloader.smallcircle, 144, 424, assetloader.smallcircle.getRegionWidth() / 2, assetloader.smallcircle.getRegionHeight() / 2, assetloader.smallcircle.getRegionWidth(), assetloader.smallcircle.getRegionHeight(), 1, 1, rot_fast);
			sb.draw(assetloader.bigcircle, 112, 392, assetloader.bigcircle.getRegionWidth() / 2, assetloader.bigcircle.getRegionHeight() / 2, assetloader.bigcircle.getRegionWidth(), assetloader.bigcircle.getRegionHeight(), 1, 1, rot_slow);
		} else {
			sb.draw(assetloader.middlecircel, 154, 434, assetloader.middlecircel.getRegionWidth() / 2, assetloader.middlecircel.getRegionHeight() / 2, assetloader.middlecircel.getRegionWidth(), assetloader.middlecircel.getRegionHeight(), 1.1f, 1.1f, 0);
			sb.draw(assetloader.smallcircle, 144, 424, assetloader.smallcircle.getRegionWidth() / 2, assetloader.smallcircle.getRegionHeight() / 2, assetloader.smallcircle.getRegionWidth(), assetloader.smallcircle.getRegionHeight(), 1.1f, 1.1f, rot_fast);
			sb.draw(assetloader.bigcircle, 112, 392, assetloader.bigcircle.getRegionWidth() / 2, assetloader.bigcircle.getRegionHeight() / 2, assetloader.bigcircle.getRegionWidth(), assetloader.bigcircle.getRegionHeight(), 1.1f, 1.1f, rot_slow);
		}
		sb.draw(assetloader.rate_menu, 254, 1196);
		sb.draw(assetloader.moregames, 396, 1196);
		sb.end();
	}

	public void dispose() {
		sb.dispose();
	}

}
