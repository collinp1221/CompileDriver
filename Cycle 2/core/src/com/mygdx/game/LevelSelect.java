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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.awt.*;
import java.io.FileNotFoundException;

public class LevelSelect implements Screen {

    MyGdxGame game;
    private Stage stage;
    private int selected = 1;

    private Texture backgroundTexture;
    private Sprite background;

    //Track Sprites and Textures (For the track icons)
    private Texture track1Texture;
    private Sprite track1Logo;
    private Texture track2Texture;
    private Sprite track2Logo;
    private Texture track3Texture;
    private Sprite track3Logo;

    private SpriteBatch batch;

    FitViewport viewport;
    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;

    LevelSelect(MyGdxGame agame)
    {
        shapeRenderer = new ShapeRenderer();

        viewport = new FitViewport(550,550);
        game = agame;
        stage = new Stage(new ScreenViewport());

        shapeRenderer.setAutoShapeType(true);

        batch = new SpriteBatch();

        //Background Initialization
        backgroundTexture = new Texture("LevelSelectBG.png");
        background = new Sprite(backgroundTexture);

        //Track 1 Logo Sprite Initialization
        track1Texture = new Texture("logos\\track1logo.png");
        track1Logo = new Sprite(track1Texture);
        track1Logo.setX(36);
        track1Logo.setY(350);

        //Track 2 Logo Sprite Initialization
        track2Texture = new Texture("logos\\track2logo.png");
        track2Logo = new Sprite(track2Texture);
        track2Logo.setX(211);
        track2Logo.setY(350);

        //Track 3 Logo Sprite Initialization
        track3Texture = new Texture("logos\\track3logo.png");
        track3Logo = new Sprite(track3Texture);
        track3Logo.setX(386);
        track3Logo.setY(350);


        final TextButton continueButton = new TextButton("Continue",game.skin,"default");
        continueButton.setWidth(100);
        continueButton.setHeight(50);
        continueButton.setPosition(400,10);
        continueButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(selected >= 1 && selected <= 5)
                {
                    String levelString ="level" + selected + ".tmx";
                    try {
                        game.setScreen(new MainScreen(game,levelString));
                    } catch (FileNotFoundException ex) {
                        System.out.print("ERROR: File Not Found Exception in LevelSelect.java");
                    }
                }
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


        stage.addActor(button1);
        stage.addActor(button2);
        stage.addActor(button3);
        stage.addActor(button4);
        stage.addActor(button5);
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

        //TODO Draw Background Image here (as a sprite)
        batch.begin();
        background.draw(batch);
        batch.end();

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


        //4, 5, 6
        shapeRenderer.rect(36,150,125,125);
        shapeRenderer.rect(212,150,125,125);
        shapeRenderer.rect(387,150,125,125);

        shapeRenderer.end();

        //Render all sprites
        batch.begin();
        track1Logo.draw(batch);
        track2Logo.draw(batch);
        track3Logo.draw(batch);
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
