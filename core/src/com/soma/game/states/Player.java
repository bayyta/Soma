package com.soma.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.soma.game.Soma;
import com.soma.game.handlers.AssetLoader;

public class Player {

	private Body body;
	private World world;
	private AssetLoader assetloader;

	private final int PPM;

	private float rot = 0;
	private float ball_rot = 0;
	private float xcoord = 0, ycoord = 0;
	private boolean down = false;
	private float xOrigin;
	private float yOrigin;
	private float power = 30;
	private double nx, ny;
	private boolean applyForces = false;
	
	public static boolean isColliding = false;
	public static float col_alpha = 1;
	
	private int stop_count = 0;
	
	private boolean below = false;
	
	private Vector3 mouse = new Vector3();

	public Player(AssetLoader assetloader, World world, int ppm) {
		this.assetloader = assetloader;
		this.world = world;
		PPM = ppm;
		float width = Soma.WIDTH;
		float height = Soma.HEIGHT;

		// ball
		BodyDef bdef = new BodyDef();
		CircleShape ball = new CircleShape();
		ball.setRadius(36f / PPM);
		bdef.position.set(new Vector2((width / 2) / PPM, height / 2 / ppm + height / 4 / PPM));
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = ball;
		fdef.restitution = 0.8f;
		fdef.density = 3.3f;
		fdef.friction = 0.1f;
		fdef.filter.categoryBits = 2;
		body.createFixture(fdef).setUserData("ball");
		body.setAwake(false);
	}
	
	public void restart() {
		applyForces = false;
		isColliding = false;
		col_alpha = 1;
		stop_count = 0;
		below = false;
		rot = 0;
		ball_rot = 0;
		xcoord = 0;
		ycoord = 0;
		down = false;
		power = 30;
		float w = Soma.WIDTH, h= Soma.HEIGHT;
		body.setTransform(new Vector2(w / 2 / PPM,  h / 2 / PPM + h / 4 / PPM), 0);
		body.setLinearVelocity(new Vector2(0, 0));
		body.setAngularVelocity(0);
		body.setAwake(false);
		world.setGravity(new Vector2(0, 9.81f));
	}

	public void update(float dt) {
		float f = body.getInertia();
		float av = body.getAngularVelocity();
		if (av > 4) body.setAngularVelocity(4);
		else if (av < -4) body.setAngularVelocity(-4);
		body.applyTorque(f, false);
		ball_rot = (float) (Math.toDegrees(body.getAngle()));
		if (!Play.gameover) updateInput();
		else if (!below) {
			world.setGravity(new Vector2(0, 9.81f));
		}
		if (isColliding) {
			col_alpha -= dt * 2f;
			if (col_alpha <= 0) isColliding = false;
		}
		if (body.getPosition().y * PPM > 1680) {
			below = true;
			Play.gameover = true;
			body.setTransform(new Vector2(720 / 2 / PPM, 1680 / PPM), 0);
			world.setGravity(new Vector2(0, 0));
			body.setLinearVelocity(new Vector2(0, 0));
		}
	}
	
	private void updateInput() {
		if (Gdx.input.justTouched()) {
			if (applyForces) {
				world.setGravity(new Vector2(0, 0));
				body.setLinearVelocity(new Vector2(0, 0));
				applyForces = false;
				stop_count++;
			}
			down = true;
			xOrigin = Gdx.input.getX();
			yOrigin = Gdx.input.getY();
		}
		if (Gdx.input.isTouched()) {
			if (down) {
				applyForces = false;
				mouse.x = Gdx.input.getX();
				mouse.y = Gdx.input.getY();
				xcoord = mouse.x - xOrigin;
				ycoord = mouse.y - yOrigin;
				double angle = Math.atan2(ycoord, xcoord);
				rot = (float) Math.toDegrees(angle) - 90;
				angle = Math.toDegrees(angle);
				angle -= 180;
				angle = Math.toRadians(angle);
				nx = Math.cos(angle);
				ny = Math.sin(angle);
				power = (float) getDistance(mouse.x, mouse.y);
				if (power > 120) power = 120;
				else if (power < 30) power = 30;
			}
		} else {
			if (down) {
				if (power > 30) {
					world.setGravity(new Vector2(0, 9.81f));
					body.applyLinearImpulse(new Vector2((float) nx * (power / 8), (float) ny * (power / 8)), body.getWorldCenter(), true);
					applyForces = true;
				}
				power = 30;
				down = false;
			}
		}
	}

	private double getDistance(float x, float y) {
		double dist = 0;
		dist = Math.sqrt(Math.abs((xOrigin - x) * (xOrigin - x) + (yOrigin - y) * (yOrigin - y)));
		return dist - 30;
	}

	public void render(SpriteBatch sb) {
		float x = body.getPosition().x;
		float y = body.getPosition().y;
		float width = assetloader.ball.getRegionWidth();
		float height = assetloader.ball.getRegionHeight();
		sb.draw(assetloader.ball, x * PPM - width / 2, y * PPM - height / 2, width / 2, height / 2, width, height, 1, 1, ball_rot);
		if (isColliding) {
			sb.setColor(1, 1, 1, col_alpha);
			sb.draw(assetloader.whiteball, x * PPM - width / 2, y * PPM - height / 2, width / 2, height / 2, width, height, 1, 1, 0);
			sb.setColor(1, 1, 1, 1);
		}
		
		if (!applyForces && power > 30) {
			sb.draw(assetloader.arrow, x * PPM - assetloader.arrow.getRegionWidth() / 2, y * PPM - assetloader.arrow.getRegionHeight() / 2 - power / 2 - 60,
					assetloader.arrow.getRegionWidth() / 2, assetloader.arrow.getRegionHeight() / 2 + power / 2 + 60,
					assetloader.arrow.getRegionWidth(), assetloader.arrow.getRegionHeight(), 1, 1, rot);
			sb.draw(assetloader.stick, x * PPM - assetloader.stick.getRegionWidth() / 2, y * PPM + assetloader.arrow.getRegionHeight() / 2 - power / 2 - 60,
					assetloader.stick.getRegionWidth() / 2, power / 2 - assetloader.arrow.getRegionHeight() / 2 + 60,
					assetloader.stick.getRegionWidth(), power / 2, 1, 1, rot);
		}
	}
	
	public void resetStopCount() {
		stop_count = 0;
	}
	
	public int getStopCount() {
		return stop_count;
	}

	public Body getBody() {
		return body;
	}

	public float getX() {
		return body.getPosition().x;
	}

	public float getY() {
		return body.getPosition().y;
	}

	
	public void dispose() {
	}
	
}
