package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
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

        final TextButton button1 = new TextButton("COMMENCE THE GAME STATE",game.skin,"default");
        button1.setWidth(100);
        button1.setHeight(50);
        button1.setPosition(100,10);
        button1.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    game.setScreen(new MainScreen(game));
                } catch (FileNotFoundException ex) {
                    System.out.print("ERROR: File Not Found Exception in LevelSelect.java");
                }
            }
        });


        stage.addActor(button1);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        //Draw some rectangles or something like that
        shapeRenderer.begin();
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(37,350,125,125);

        shapeRenderer.rect(212,350,125,125);
        shapeRenderer.rect(387,350,125,125);
        shapeRenderer.end();
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
