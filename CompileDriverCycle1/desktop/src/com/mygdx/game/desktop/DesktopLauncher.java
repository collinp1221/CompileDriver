package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

import java.io.FileNotFoundException;

public class DesktopLauncher {
	public static void main (String[] arg) throws FileNotFoundException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MyGdxGame(), config);

		config.width=550;
		config.height=550;

		config.foregroundFPS = 60; //Set max FPS to 60
	}
}
