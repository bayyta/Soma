package com.soma.game.states;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.soma.game.handlers.AssetLoader;

public class DeathLight {
	
	private Random random;
	private AssetLoader assetloader;
	private final int PPM;
	
	private float width, height;
	private float x = 0, y = 0;
	private float rot;
	
	public DeathLight(AssetLoader assetloader, float x, float y, int ppm) {
		this.assetloader = assetloader;
		this.PPM = ppm;
		random = new Random();
		width = assetloader.death_light.getRegionWidth();
		height = random.nextInt(700) + 50;
		rot = random.nextInt(360);
	}
	
	public void update(float dt, float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void render(SpriteBatch sb) {
		sb.draw(assetloader.death_light, x * PPM - width / 2, y * PPM - height / 2  - 75 / 2, width / 2, height / 2 + 75 / 4, width, height, 1, 1, rot);
	}

}
