package com.soma.game.handlers;

public class Box {

	protected float x;
	protected float y;
	protected float width;
	protected float height;

	public boolean contains(float x, float y) {
		return x >= this.x && x < this.x + width && y >= this.y && y < this.y + height;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}
