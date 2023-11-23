package com.soma.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.soma.game.Soma;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Soma.WIDTH;
		config.height = Soma.HEIGHT;
		config.title = Soma.TITLE;
		config.vSyncEnabled = false;
		config.backgroundFPS = -1;
		config.foregroundFPS = 60;
		new LwjglApplication(new Soma(), config);
	}
}
