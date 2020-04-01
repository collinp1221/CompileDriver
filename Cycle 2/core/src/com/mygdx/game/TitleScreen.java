package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.FileNotFoundException;

public class TitleScreen implements Screen {
    MyGdxGame game;
    private Stage stage;
    SpriteBatch batchpic;
    Texture img;

    public TitleScreen(MyGdxGame agame)
    {
        game = agame;
        stage = new Stage(new ScreenViewport());

        final TextButton begin = new TextButton("Begin",game.skin,"default");
        begin.setWidth(100);
        begin.setHeight(50);
        begin.setPosition(225,10);
        begin.addListener(new ClickListener()
        {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                //try {
                    game.setScreen( new LevelSelect(game));
                //}
                //catch (FileNotFoundException ex)
                //{
                //    System.out.print("Hello");
                //}
            }
        });

        stage.addActor(begin);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

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
        stage.dispose();
    }
}
