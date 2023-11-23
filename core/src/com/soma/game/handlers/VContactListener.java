package com.soma.game.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.soma.game.states.Play;
import com.soma.game.states.Player;

public class VContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Play.collisions++;
		Player.isColliding = true;
		Player.col_alpha = 1;
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
