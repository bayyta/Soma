package com.soma.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.soma.game.Soma;

public class Sides {

	private final int PPM;
	private Body body;
	private Body body2;
	private float y;

	private float width = Soma.WIDTH, height = Soma.HEIGHT;

	public Sides(World world, int ppm, float y) {
		this.y = y;
		PPM = ppm;

		BodyDef bdef = new BodyDef();
		bdef.position.set(new Vector2(0 / PPM, y / PPM));
		bdef.type = BodyType.KinematicBody;
		body = world.createBody(bdef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(10 / PPM, height / PPM / 2);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = 4;
		body.createFixture(fdef).setUserData("side");

		// second
		bdef.position.set(new Vector2(width / PPM, y / PPM));
		bdef.type = BodyType.KinematicBody;
		body2 = world.createBody(bdef);
		body2.createFixture(fdef);
	}

	public void update(float dt, float y) {
		this.y = y / PPM;
		body.setTransform(0, this.y, 0);
		body2.setTransform(width / PPM, this.y, 0);
	}

	public void render(SpriteBatch sb) {

	}

}
