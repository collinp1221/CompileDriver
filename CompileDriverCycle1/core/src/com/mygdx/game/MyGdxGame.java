package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch; //SpriteBatch that stores all sprites to be used
	Texture img; //Background Image
	Texture carImg; //Player 1 car sprite
	private Stage stage;
	private Skin skin;

	public int tickCount = 0;
	public int playerCount = 1;
	Car player1 = new Car(1);
	//Car player2 = new Car(2);
	//Car player3 = new Car(3);
	//Car player4 = new Car(4);


	public MyGdxGame() throws FileNotFoundException {
	}

	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage(new ScreenViewport());


		final TextButton button = new TextButton("Begin",skin,"default");
		button.setWidth(100);
		button.setHeight(50);
		button.setPosition(200,10);

		button.addListener(new ClickListener()
		{

			@Override
			public void clicked(InputEvent event, float x, float y) {
				player1.setAngle(90);//need to make car move when button is clicked
			}
		});
		Gdx.input.setInputProcessor(stage);
		stage.addActor(button);

		Gdx.graphics.setTitle("Car Game Demo"); //Set the title to be something more interesting than MyGdxGame

		batch = new SpriteBatch();
		carImg = new Texture("car1.png");
		img = new Texture("test_level2.png"); //TODO Get this from user input at some point
		player1.setSprite(carImg); //Sets the sprite for player 1
		player1.setMapTexture(img); //Assigns the map texture for player 1 //TODO Maybe consider removing or modifying this?

		//TODO Remove these commands, and use the other declaration of the Car object to set starting X and Y
		player1.setStartingX(290);
		player1.setStartingY(160);

		//TODO Setup players 2-4 if they are in the game (Dev Cycle 1)
	}

	@Override
	public void render () {
		//Sets the background to be black (just in case of issues drawing the background)
		Gdx.gl.glClearColor(0, 0, 0, 1);

		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Begins the SpriteBatch, to allow for drawing sprites to the screen

		batch.begin();
		batch.draw(img, 0, 0); //Draw Background

		//Does a single gametick for player 1's car
		player1.doTick();

		//DRAW SPRITES
		//Draw the sprite of player 1's car
		player1.getSprite().draw(batch);

		//Ends the SpriteBatch. After this, nothing can be drawn until the batch is started again
		batch.end();

		//GUI ELEMENTS
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
