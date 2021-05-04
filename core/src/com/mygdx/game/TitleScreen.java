package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.FileNotFoundException;
import java.util.logging.Level;

public class TitleScreen implements Screen {
    MyGdxGame game;
    private Stage stage;
    SpriteBatch batchpic;
    private Texture img;
    private Texture titleScreen;
    private Sprite titleScreenSprite;

    public TitleScreen(MyGdxGame agame)
    {
        game = agame;
        stage = new Stage(new ScreenViewport());
        batchpic = new SpriteBatch();

        final TextButton begin = new TextButton("Begin",game.skin,"default");
        begin.setWidth(100);
        begin.setHeight(50);
        begin.setPosition(200,40);
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

        final TextButton settings = new TextButton("Settings",game.skin,"default");
        settings.setWidth(100);
        settings.setHeight(50);
        settings.setPosition(400,40);
        settings.addListener(new ClickListener()
        {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                //try {
                game.setScreen( new OptionsScreen(game));
                //}
                //catch (FileNotFoundException ex)
                //{
                //    System.out.print("Hello");
                //}
            }
        });

        titleScreen = new Texture("TitleScreen.png");
        titleScreenSprite = new Sprite(titleScreen);

        stage.addActor(begin);
        stage.addActor(settings);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batchpic.begin();
        titleScreenSprite.draw(batchpic);
        batchpic.end();
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
