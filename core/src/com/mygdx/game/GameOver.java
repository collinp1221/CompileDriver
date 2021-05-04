package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.FileNotFoundException;

public class GameOver implements Screen
{
    private MyGdxGame game;
    private SpriteBatch batch;
    private Stage stage;

    private Texture backgroundTexture;
    private Sprite backgroundSprite;

    private BitmapFont winFont;
    private BitmapFont buttonFont;
    private TextButton.TextButtonStyle style;

    private int winNum; //Number of winning player
    private String level;

    public GameOver(MyGdxGame agame, int winner, String levelPath)
    {
        //Create important objects
        game = agame; //Core "game" object that can be used to change screens
        winNum = winner; //Number of winning player
        level = levelPath;
        batch = new SpriteBatch(); //Spritebatch used to render sprites

        stage = new Stage(new ScreenViewport()); //Stage used to render actors (buttons, etc.)

        //Load background texture
        backgroundTexture = new Texture("gameOver.png");
        backgroundSprite = new Sprite(backgroundTexture);


        //Generate font for "Player x Won" font
        FreeTypeFontGenerator fgen = new FreeTypeFontGenerator(Gdx.files.internal("Fonts\\Retro Gaming.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter1.size = 68;
        parameter1.color = Color.LIGHT_GRAY;
        winFont = fgen.generateFont(parameter1);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 4;
        parameter2.color = Color.BLACK;
        buttonFont = fgen.generateFont(parameter2);

        //Initialize and modify style of Textbuttons
        //style = new TextButton.TextButtonStyle();
        //style.fontColor = Color.WHITE;
        //style.downFontColor = Color.DARK_GRAY;
        //style.font = buttonFont;

        //Load Main Menu button for the Game Over pop-up
        final TextButton mainMenuButton = new TextButton("Main Menu",game.skin,"default");
        mainMenuButton.setWidth(250);
        mainMenuButton.setHeight(100);
        mainMenuButton.setPosition(50,50);
        mainMenuButton.setColor(Color.RED);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelect(game));
            }
        });
        //Load New Game button for the Game Over pop-up
        final TextButton playAgainButton = new TextButton("Play Again", game.skin, "default");
        playAgainButton.setWidth(250);
        playAgainButton.setHeight(100);
        playAgainButton.setPosition(400,50);
        playAgainButton.setColor(Color.GREEN);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try
                {
                    game.setScreen(new MainScreen(game, level));
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        });


        stage.addActor(mainMenuButton);
        stage.addActor(playAgainButton);


    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        backgroundSprite.draw(batch);
        winFont.draw(batch, "Player " + winNum + " Won!", 60, 700);
        batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int i, int i1)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}
