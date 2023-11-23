package com.soma.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.soma.game.handlers.AssetLoader;
import com.soma.game.handlers.InputHandler;

public class Soma extends ApplicationAdapter {
	public static int WIDTH = 720;
	public static int HEIGHT = 1280;
	public static String TITLE = "Soma";
	private FPSLogger fps = new FPSLogger();
	
	private AssetLoader assetloader;
	private InputHandler inputhandler;
	private Game game;
	
	@Override
	public void create () {
		System.out.println("create");
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		
		assetloader = new AssetLoader();
		inputhandler = new InputHandler();
		Gdx.input.setInputProcessor(inputhandler);
		game = new Game(assetloader, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		fps.log();
		game.update(Gdx.graphics.getDeltaTime());
		game.render();
	}
	
	@Override
	public void resize(int width, int height) {
		System.out.println("resize");
	}
	
	@Override
	public void pause() {
		System.out.println("pause");
	}
	
	@Override
	public void resume() {
		System.out.println("resume");
	}
	
	@Override
	public void dispose() {
		game.dispose();
		assetloader.dispose();
		System.out.println("dispose");
	}
}
