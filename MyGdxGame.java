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

public class MyGdxGame extends Game{

	private SpriteBatch batch; //SpriteBatch that stores all sprites to be used
	private Texture carImg; //Player 1 car sprite

	public int selected = 1;//For level selector
	private OrthographicCamera camera;

	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer tiledMapRenderer;

	//Various Tile Layers of the TileMap
	private TiledMapTileLayer backgroundLayer1;
	private TiledMapTileLayer backgroundLayer2;
	private TiledMapTileLayer foregroundLayer;
	//private MapLayer collisionLayer;

	private MapObjects objects;

	private TiledMapTileSet tileset;

	public int tickCount = 0; //Counts the number of ticks that have occurred
	public int playerCount = 1; //Tracks the number of players in the game
	private String workingDirectory = System.getProperty("user.dir");
	private String absoluteFilePath = workingDirectory + File.separator + "ai" + File.separator + "defaultAI.txt";

	public Skin skin;
	public float brightness = 0F;

	Car player1;
	//Car player2 = new Car(2);
	//Car player3 = new Car(3);
	//Car player4 = new Car(4);



	public MyGdxGame()
	{
	}

	//Everything that happens as the program is initialized
	@Override
	public void create () {

		skin = new Skin(Gdx.files.internal("uiskin.json"));

		Gdx.graphics.setTitle("Compile Driver | Cycle 2");
		Gdx.graphics.setResizable(false); //Prevent external resizing of window (by user)

		this.setScreen(new TitleScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

}
