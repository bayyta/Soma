package com.soma.game.states;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.soma.game.Soma;
import com.soma.game.handlers.AssetLoader;

public class Obstacles {

	private Body body;
	private Body body2;
	private World world;
	private AssetLoader assetloader;

	private final int PPM;
	private final float GAP = 140f;
	private float width;
	private float width2;
	private float height = 33f;
	private float y;
	private int number;

	private Random random = new Random();
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();

	public Obstacles(AssetLoader assetloader, World world, int ppm, float y, int number, BitmapFont font) {
		this.assetloader = assetloader;
		this.y = y;
		this.world = world;
		this.number = number;
		this.font = font;
		String s = Integer.toString(number);
		layout.setText(font, s);
		PPM = ppm;

		// first
		BodyDef bdef = new BodyDef();
		width = random.nextInt(720 - Math.round(GAP) - 40) + 20;
		if (number == 1) width = random.nextInt(720 - Math.round(GAP) - 300) + 150;
		bdef.position.set(new Vector2(width / PPM / 2, y / PPM));
		bdef.type = BodyType.KinematicBody;
		body = world.createBody(bdef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / PPM / 2, height / PPM / 2);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = 4;
		body.createFixture(fdef).setUserData("obstacle");

		// second
		width2 = 720 - width - GAP + 1;
		bdef.position.set(new Vector2((width + GAP + width2 / 2) / PPM, y / PPM));
		bdef.type = BodyType.KinematicBody;
		body2 = world.createBody(bdef);
		shape.setAsBox(width2 / PPM / 2, height / PPM / 2);
		fdef.shape = shape;
		body2.createFixture(fdef).setUserData("obstacle");
	}

	public void update(float dt) {
	}

	public void render(SpriteBatch sb) {
		String s = Integer.toString(number);
		float w = width;
		if (w > 720 / 4) w += 2;
		if (w > 720 / 2) w += 2;
		if (w > 720 - 200) w += 2;
		font.draw(sb, s, Soma.WIDTH / 2 - layout.width / 2, y - 480 + layout.height / 2 + 20);

		sb.draw(assetloader.obstacle, -5, y - assetloader.obstacle.getRegionHeight() / 2, w + 5, assetloader.obstacle.getRegionHeight());
		sb.draw(assetloader.obstacle, w + GAP - 6, y - assetloader.obstacle.getRegionHeight() / 2, width2 + 11, assetloader.obstacle.getRegionHeight());
	}

	public float getY() {
		return y;
	}

	public boolean shouldRemove(float y, float height) {
		if (this.y - height > y) {
			world.destroyBody(body);
			world.destroyBody(body2);
			body.setUserData(null);
			body = null;
			body2.setUserData(null);
			body2 = null;
			return true;
		}
		return false;
	}
	
	public void remove() {
		world.destroyBody(body);
		world.destroyBody(body2);
		body.setUserData(null);
		body = null;
		body2.setUserData(null);
		body2 = null;
	}

	public boolean contains(float y, float height) {
		if (y * PPM <= this.y && y * PPM >= this.y - height) return true;

		return false;
	}

	public int getCount() {
		return number;
	}

	public void dipose() {
	}

}
