package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

public class LevelSelect implements Screen {

    MyGdxGame game; //Game object
    private Stage stage;
    private int selected = 1; //Tracks the currently selected level
    private boolean validTMXSelected = false; //Tracks whether or not the selected TileMap file is correct or not.

    private Texture backgroundTexture; //Texture to be applied to the background sprite
    private Sprite background; //Image to be rendered as the background

    //Track Sprites and Textures (For the track icons)
    private Texture track1Texture; //Texture for the Track 1 icon
    private Sprite track1Logo; //Track 1 icon Sprite
    private Texture track2Texture; //Texture for the Track 2 icon
    private Sprite track2Logo;//Track 2 icon Sprite
    private Texture track3Texture; //Texture for the Track 3 icon
    private Sprite track3Logo;//Track 3 icon Sprite
    private Texture track4Texture; //Texture for the Track 4 icon
    private Sprite track4Logo;//Track 4 icon Sprite
    private Texture track5Texture; //Texture for the Track 5 icon
    private Sprite track5Logo;//Track 5 icon Sprite
    private Texture customTrackTexture; //Custom Track icon Texture
    private Sprite customTrackSprite; //Custom Track icon Sprite

    private SpriteBatch batch; //Batch through which all sprites will be rendered (required LibGDX thing)

    FitViewport viewport; //Viewport through which rendered sprites will be viewed
    ShapeRenderer shapeRenderer; //Used to render the red outline around the selected track
    OrthographicCamera camera; //Camera through which the menu will be viewed (works with viewport)

    JFileChooser fileChooser = new JFileChooser(); //Object that is used to open a file select window for selecting a custom track
    private JFrame frame; //The window in which the file select window will be opened
    File selectedFile; //Will store the selected TileMap file

    //Create function. Runs once
    LevelSelect(MyGdxGame agame)
    {
        shapeRenderer = new ShapeRenderer();

        //Set viewport to be 550x550
        viewport = new FitViewport(550,550);
        game = agame;
        stage = new Stage(new ScreenViewport());

        shapeRenderer.setAutoShapeType(true); //Allows the shaperenderer to render objects in less time

        batch = new SpriteBatch();

        //Set filechooser to the user's home directory
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        //Background Initialization
        backgroundTexture = new Texture("LevelSelectBG.png");
        background = new Sprite(backgroundTexture);

        //Track 1 Logo Sprite Initialization
        track1Texture = new Texture("track1logo.png");
        track1Logo = new Sprite(track1Texture);
        track1Logo.setX(36);
        track1Logo.setY(350);

        //Track 2 Logo Sprite Initialization
        track2Texture = new Texture("track2logo.png");
        track2Logo = new Sprite(track2Texture);
        track2Logo.setX(211);
        track2Logo.setY(350);

        //Track 3 Logo Sprite Initialization
        track3Texture = new Texture("track3logo.png");
        track3Logo = new Sprite(track3Texture);
        track3Logo.setX(386);
        track3Logo.setY(350);

        //Track 4 Logo Sprite Initialization
        track4Texture = new Texture("track4logo.png");
        track4Logo = new Sprite(track4Texture);
        track4Logo.setX(36);
        track4Logo.setY(150);

        //Track 5 Logo Sprite Initialization
        track5Texture = new Texture("track5logo.png");
        track5Logo = new Sprite(track5Texture);
        track5Logo.setX(211);
        track5Logo.setY(150);

        //Custom Track Sprite Initialization
        customTrackTexture = new Texture("customTrackLogo.png");
        customTrackSprite = new Sprite(customTrackTexture);
        customTrackSprite.setX(386);
        customTrackSprite.setY(150);

        //Continue Button
        final TextButton continueButton = new TextButton("Continue",game.skin,"default");
        continueButton.setWidth(100);
        continueButton.setHeight(50);
        continueButton.setPosition(400,10);
        continueButton.setColor(Color.GREEN);
        continueButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                //If one of the 5 preset levels are selected:
                if(selected >= 1 && selected <= 5)
                {
                    String levelString ="C:\\Program Files\\CompileDriver\\TileMaps\\level" + selected + ".tmx";
                    try {
                        game.setScreen(new MainScreen(game,levelString));
                    } catch (FileNotFoundException ex) {
                        System.out.print("ERROR: File Not Found Exception in LevelSelect.java");
                    }
                }
                //If the Custom Track button is selected:
                else if(selected == 6)
                {
                    //TODO All TMX Checking code here
                    //Attempt to open the TiledMap file. If it fails, set validTMXSelected to false and change selected to 0 (No track selected)
                    try
                    {
                        TiledMap tiledMap = new TmxMapLoader().load(selectedFile.getAbsolutePath());
                        //TODO More tiledMap checking here

                        game.setScreen(new MainScreen(game, selectedFile));
                    }
                    //If an error is caught, then output an error message, and select nothing
                    catch(Exception e)
                    {
                        System.out.println("ERROR: Unable to load the selected file");
                        validTMXSelected = false;
                        selected = 0;
                    }

                }
                //If something unexpected is selected, output an error message
                else
                {
                    System.out.println("ERROR: Invalid input!!");
                }
            }
        });

        //Select Track 1
        final TextButton button1 = new TextButton("Track 1",game.skin,"default");
        button1.setWidth(100);
        button1.setHeight(50);
        button1.setPosition(50,290);
        button1.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                selected = 1;
            }
        });

        //Select Track 2
        final TextButton button2 = new TextButton("Track 2",game.skin,"default");
        button2.setWidth(100);
        button2.setHeight(50);
        button2.setPosition(223,290);
        button2.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                selected = 2;
            }
        });

        //Select Track 3
        final TextButton button3 = new TextButton("Track 3",game.skin,"default");
        button3.setWidth(100);
        button3.setHeight(50);
        button3.setPosition(398,290);
        button3.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                selected = 3;
            }
        });

        //Select Track 4
        final TextButton button4 = new TextButton("Track 4",game.skin,"default");
        button4.setWidth(100);
        button4.setHeight(50);
        button4.setPosition(50,90);
        button4.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                selected = 4;
            }
        });

        //Select Track 5
        final TextButton button5 = new TextButton("Track 5",game.skin,"default");
        button5.setWidth(100);
        button5.setHeight(50);
        button5.setPosition(223,90);
        button5.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                selected = 5;
            }
        });


        //Custom Track button
        final TextButton customButton = new TextButton("Custom Track",game.skin,"default");
        customButton.setWidth(100);
        customButton.setHeight(50);
        customButton.setPosition(398,90);
        customButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                try
                {
                    int result = fileChooser.showOpenDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        selectedFile = fileChooser.getSelectedFile();
                        //If the selected file ends in ".tmx":
                        if(selectedFile.getAbsolutePath().substring(selectedFile.getAbsolutePath().length() - 4).equals(".tmx"))
                        {
                            System.out.println("Selected a TMX file");
                            selected = 6;
                            validTMXSelected = true;
                        }
                        else
                        {
                            validTMXSelected = false;
                            System.out.println("Invalid File Selected!");
                        }
                    }

                }
                catch(Exception e)
                {
                    System.out.println("ERROR: Something went wrong in file selection");
                    selected = 1;
                }
            }
        });


        //Back Button. When pressed, return to the Title Screen
        final TextButton backButton = new TextButton("Back",game.skin,"default");
        backButton.setWidth(100);
        backButton.setHeight(50);
        backButton.setPosition(75,10);
        backButton.setColor(Color.GREEN);
        backButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TitleScreen(game));
            }
        });


        //Initialize all Actors
        stage.addActor(button1);
        stage.addActor(button2);
        stage.addActor(button3);
        stage.addActor(button4);
        stage.addActor(button5);
        stage.addActor(customButton);
        stage.addActor(backButton);
        stage.addActor(continueButton);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }



    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw the background image
        batch.begin();
        background.draw(batch);
        batch.end();

        //All stage actions occur here
        stage.act();
        stage.draw();

        //Draw red outline around selected level
        shapeRenderer.begin();
        shapeRenderer.setColor(Color.WHITE);
        if(selected == 1) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(36, 349, 127,127);
            shapeRenderer.rect(35, 348, 129,129);
            shapeRenderer.rect(34, 347, 131,131);
            shapeRenderer.rect(33, 346, 133,133);
            shapeRenderer.setColor(Color.WHITE);
        }

        if(selected == 2) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(211, 349, 127,127);
            shapeRenderer.rect(210, 348, 129,129);
            shapeRenderer.rect(209, 347, 131,131);
            shapeRenderer.rect(208, 346, 133,133);
            shapeRenderer.setColor(Color.WHITE);
        }

        if(selected == 3) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(386, 349, 127,127);
            shapeRenderer.rect(385, 348, 129,129);
            shapeRenderer.rect(384, 347, 131,131);
            shapeRenderer.rect(383, 346, 133,133);
            shapeRenderer.setColor(Color.WHITE);
        }

        if(selected == 4) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(36, 149, 127,127);
            shapeRenderer.rect(35, 148, 129,129);
            shapeRenderer.rect(34, 147, 131,131);
            shapeRenderer.rect(33, 146, 133,133);
            shapeRenderer.setColor(Color.WHITE);
        }

        if(selected == 5) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(211, 149, 127,127);
            shapeRenderer.rect(210, 148, 129,129);
            shapeRenderer.rect(209, 147, 131,131);
            shapeRenderer.rect(208, 146, 133,133);
            shapeRenderer.setColor(Color.WHITE);
        }

        if(selected == 6)
        {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(386, 149, 127,127);
            shapeRenderer.rect(385, 148, 129,129);
            shapeRenderer.rect(384, 147, 131,131);
            shapeRenderer.rect(383, 146, 133,133);
            shapeRenderer.setColor(Color.WHITE);
        }

        shapeRenderer.end();

        //Render all sprites (Level Select Icons)
        batch.begin();
        track1Logo.draw(batch);
        track2Logo.draw(batch);
        track3Logo.draw(batch);
        track4Logo.draw(batch);
        track5Logo.draw(batch);
        customTrackSprite.draw(batch);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}