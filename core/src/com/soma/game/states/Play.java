package com.soma.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.soma.game.Game;
import com.soma.game.Soma;
import com.soma.game.handlers.AssetLoader;
import com.soma.game.handlers.VContactListener;

public class Play {

	private AssetLoader assetloader;

	private World world;
	private Box2DDebugRenderer renderer;
	private boolean debug = false;
	private OrthographicCamera b2dcam;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	private SpriteBatch sb;
	private Game game;

	private int score = 0;
	private boolean new_highScore = false;
	private int obs_count;
	private final int PPM = 100;
	private boolean start = false;
	public static boolean gameover = false;

	public static int collisions = 0;

	private Player player;
	private Sides sides;
	private Array<Obstacles> list = new Array<Obstacles>();
	private float currentY;
	private float yOffset = 50f;
	private float yStartOffset = 90f;
	private int height_between_obstacles = 480;
	private int current_obs;

	private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Archive.otf"));
	private FreeTypeFontParameter parameter = new FreeTypeFontParameter();
	private GlyphLayout layout = new GlyphLayout();

	private BitmapFont font_archive_big;
	private BitmapFont font_archive_small;
	private BitmapFont font_archive_small_green;

	public Play(Game game, AssetLoader assetloader) {
		this.game = game;
		this.assetloader = assetloader;
		sb = new SpriteBatch();
		obs_count = 0;
		current_obs = 0;

		world = new World(new Vector2(0, 9.81f), true);
		world.setContactListener(new VContactListener());
		if (debug) renderer = new Box2DDebugRenderer();
		b2dcam = new OrthographicCamera();
		cam = game.getCam();
		hudCam = game.getHudCam();
		float width = Soma.WIDTH;
		float height = Soma.HEIGHT;
		b2dcam.setToOrtho(true, width / PPM, height / PPM);

		parameter.size = 320;
		parameter.flip = true;
		parameter.color = new Color(1f, 1f, 1f, 0.15f);
		parameter.minFilter = TextureFilter.Linear;
		parameter.magFilter = TextureFilter.Linear;
		font_archive_big = generator.generateFont(parameter);
		parameter.size = 50;
		parameter.color = new Color(0xf5f5f5ff);
		font_archive_small = generator.generateFont(parameter);
		parameter.color = new Color(0x36c2a3ff);
		font_archive_small_green = generator.generateFont(parameter);

		player = new Player(assetloader, world, PPM);
		sides = new Sides(world, PPM, cam.position.y);
		list.add(new Obstacles(assetloader, world, PPM, height / 2, 1, font_archive_big));
		obs_count++;

		currentY = player.getY() - yOffset / PPM - yStartOffset / PPM;
		cam.position.set(Soma.WIDTH / 2, currentY * PPM, 0);
		b2dcam.position.set(360f / PPM, currentY, 0);
		cam.update();
		b2dcam.update();
	}

	public void restart() {
		new_highScore = false;
		obs_count = 0;
		current_obs = 0;
		score = 0;
		start = false;
		gameover = false;
		collisions = 0;
		for (int i = 0; i < list.size; i++) {
			list.get(i).remove();
		}
		list.clear();
		list.add(new Obstacles(assetloader, world, PPM, Soma.HEIGHT / 2, 1, font_archive_big));
		obs_count++;
		player.restart();
		currentY = player.getY() - yOffset / PPM - yStartOffset / PPM;
		cam.position.set(Soma.WIDTH / 2, currentY * PPM, 0);
		b2dcam.position.set(360f / PPM, currentY, 0);
		cam.update();
		b2dcam.update();
	}

	public void update(float dt) {
		if (!start) {
			alpha -= dt;
			scale += dt;
			if (alpha < 0) {
				alpha = 1;
				scale = 1;
			}
			if (Gdx.input.justTouched()) {
				start = true;
			}
		}
		world.step(dt, 8, 3);
		player.update(dt);
		sides.update(dt, cam.position.y);
		updateObstacles(dt);
		updateCam();

		if (collisions >= 2) gameover = true;
		if (player.getStopCount() >= 1 && current_obs == 0) gameover = true;
		if (player.getStopCount() > 1) gameover = true;
		if (gameover) {
			updateGameOver();
			if (score > assetloader.getHighScore()) {
				new_highScore = true;
				assetloader.setHighScore(score);
			}
		}
	}

	private void updateGameOver() {
		if (Gdx.input.justTouched()) {
			Vector3 mouse = new Vector3();
			mouse.x = Gdx.input.getX();
			mouse.y = Gdx.input.getY();
			hudCam.unproject(mouse);
			if (mouse.y >= 785 && mouse.y <= 785 + assetloader.home.getRegionHeight()) {
				if (mouse.x >= 193 && mouse.x <= 193 + assetloader.home.getRegionWidth()) {
					game.startfade(Game.STATE.MENU, "menu");
				} else if (mouse.x >= 325 && mouse.x <= 325 + assetloader.rate.getRegionWidth()) {
					Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.soma.game.android");
				} else if (mouse.x >= 458 && mouse.x <= 458 + assetloader.restart.getRegionWidth()) {
					restart();
				}
			}
		}
	}

	private void updateObstacles(float dt) {
		if (list.size < 5) {
			obs_count++;
			list.add(new Obstacles(assetloader, world, PPM, list.get(list.size - 1).getY() - height_between_obstacles, obs_count, font_archive_big));
		}
		if (start && !gameover) {
			for (int i = 0; i < list.size; i++) {
				if (list.get(i).shouldRemove(cam.position.y + Soma.HEIGHT / 1.5f, height_between_obstacles)) {
					list.removeIndex(i);
					continue;
				}
				list.get(i).update(dt);
				if (current_obs == 1 && player.getY() * PPM > list.get(0).getY()) gameover = true;
				if (list.get(i).contains(player.getY(), height_between_obstacles)) {
					if (current_obs > list.get(i).getCount()) {
						score = current_obs;
						gameover = true;
					} else if (current_obs != list.get(i).getCount()) {
						collisions = 0;
						player.resetStopCount();
						score = list.get(i).getCount();
					}
					current_obs = list.get(i).getCount();
				}
			}
		}
	}

	private void updateCam() {
		if (currentY > player.getY() - yOffset / PPM) {
			currentY = player.getY() - yOffset / PPM;
			cam.position.set(Soma.WIDTH / 2, currentY * PPM, 0);
			b2dcam.position.set(360f / PPM, currentY, 0);
			cam.update();
			b2dcam.update();
		}
	}

	public void render() {
		sb.begin();
		sb.disableBlending();
		sb.setProjectionMatrix(hudCam.combined);
		sb.draw(assetloader.menubackground, 0, 0);
		sb.setProjectionMatrix(cam.combined);
		sb.enableBlending();
		for (int i = 0; i < list.size; i++) {
			list.get(i).render(sb);
		}
		drawCircle(sb);
		player.render(sb);
		sb.setColor(1, 1, 1, 1);
		sb.setProjectionMatrix(hudCam.combined);
		if (gameover) drawGameOver(sb);
		sb.end();
		if (debug) renderer.render(world, b2dcam.combined);
	}

	private float alpha = 1;
	private float scale = 1;

	private void drawCircle(SpriteBatch sb) {
		float w = assetloader.circle.getRegionWidth();
		float h = assetloader.circle.getRegionHeight();
		sb.setColor(1, 1, 1, alpha);
		if (!start) sb.draw(assetloader.circle, player.getX() * PPM - w / 2, player.getY() * PPM - h / 2, w / 2, h / 2, w, h, scale, scale, 0);
		sb.setColor(1, 1, 1, 1);
	}

	private void drawGameOver(SpriteBatch sb) {
		sb.draw(assetloader.game_over_circle, Soma.WIDTH / 2 - assetloader.game_over_circle.getRegionWidth() / 2, 450);
		sb.draw(assetloader.game_over, Soma.WIDTH / 2 - assetloader.game_over.getRegionWidth() / 2, 332, assetloader.game_over.getRegionWidth(), assetloader.game_over.getRegionHeight());

		String s = Integer.toString(score);
		layout.setText(font_archive_small, s);
		sb.draw(assetloader.score, Soma.WIDTH / 2 - assetloader.score.getRegionWidth() / 2, 526);
		font_archive_small_green.draw(sb, s, Soma.WIDTH / 2 - layout.width / 2, 556);

		String b = Integer.toString(assetloader.getHighScore());
		layout.setText(font_archive_small, b);
		sb.draw(assetloader.best, Soma.WIDTH / 2 - assetloader.best.getRegionWidth() / 2, 622);
		font_archive_small.draw(sb, b, Soma.WIDTH / 2 - layout.width / 2, 652);
		
		if (new_highScore) sb.draw(assetloader.new_best, 267, 619);

		sb.draw(assetloader.home, 193, 785);
		sb.draw(assetloader.rate, 325, 785);
		sb.draw(assetloader.restart, 458, 785);
	}

	public void dispose() {
		world.dispose();
		player.dispose();
		for (int i = 0; i < list.size; i++) {
			list.get(i).dipose();
		}
		generator.dispose();
		font_archive_small.dispose();
		font_archive_small_green.dispose();
		font_archive_big.dispose();
	}
}
