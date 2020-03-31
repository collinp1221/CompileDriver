package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.io.File;
import java.io.FileNotFoundException;

public class MyGdxGame extends Game {
	//Car player1;
	//Car player2 = new Car(2);
	//Car player3 = new Car(3);
	//Car player4 = new Car(4);


	public Skin skin;


	public MyGdxGame(){
	}

	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		Gdx.graphics.setTitle("Compile Driver | Cycle 1");
		Gdx.graphics.setResizable(false); //Prevent external resizing of window (by user)
		this.setScreen(new TitleScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}

}
