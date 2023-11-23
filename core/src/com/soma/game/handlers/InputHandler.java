package com.soma.game.handlers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class InputHandler implements InputProcessor {
	
	public static boolean touchDown = false;
	public static Vector2 coordinates = new Vector2();

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touchDown = true;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touchDown = false;
		coordinates.x = screenX;
		coordinates.y = screenY;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
