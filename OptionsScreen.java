package com.mygdx.game;


import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class OptionsScreen extends ScreenAdapter {
    MyGdxGame game;
    SpriteBatch batchs;
    Texture imgs;
    ShaderBatch gammer;
    Sound buzzer;
    Audio speaker;

    private Stage optionStage;
    public OptionsScreen(MyGdxGame agame)
    {
        this.game = agame;
        gammer = new ShaderBatch(100);
        buzzer = Gdx.audio.newSound(Gdx.files.internal("buzzer.mp3"));
        if (!gammer.isCompiled) {
            System.err.println(gammer.log); //due to GL11 or an error compiling shader
            //if we try using it now, it will behave just like a regular sprite batch
        }
        batchs = new SpriteBatch();
        imgs = new Texture("badlogic.jpg");
        optionStage= new Stage(new ScreenViewport());

        final TextButton back = new TextButton("back",game.skin,"default");
        back.setWidth(100);
        back.setHeight(50);
        back.setPosition(330,10);
        back.addListener(new ClickListener()
        {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TitleScreen(game));
            }
        });

        final TextButton low = new TextButton("low",game.skin,"default");
        low.setWidth(100);
        low.setHeight(50);
        low.setPosition(330,80);
        low.addListener(new ClickListener()
        {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.brightness= -0.5F;//dark
            }
        });

        final TextButton high = new TextButton("High",game.skin,"default");
        high.setWidth(100);
        high.setHeight(50);
        high.setPosition(330,200);
        high.addListener(new ClickListener()
        {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.brightness = 0.5F;//lighter
            }
        });

        final TextButton normal = new TextButton("Normal",game.skin,"default");
        normal.setWidth(100);
        normal.setHeight(50);
        normal.setPosition(330,140);
        normal.addListener(new ClickListener()
        {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.brightness = 0F;
            }
        });

        final TextButton buzz = new TextButton("Buzz",game.skin,"default");
        buzz.setWidth(100);
        buzz.setHeight(50);
        buzz.setPosition(330,260);
        buzz.addListener(new ClickListener()
        {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                buzzer.play(0.2F);
            }
        });


        optionStage.addActor(back);
        optionStage.addActor(low);
        optionStage.addActor(high);
        optionStage.addActor(normal);
        optionStage.addActor(buzz);

    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gammer.brightness = game.brightness; // 0.0 -> no change
        gammer.contrast = 1f; // 1.0 -> no change
        gammer.begin();
        gammer.draw(imgs,1F,1F);
        gammer.end();

        /*batchs.setColor(game.brightness,game.brightness,game.brightness,1F);//makes sprite gamma
        batchs.begin();
        batchs.draw(imgs, 50, 70);
        batchs.end();*/
        optionStage.act();
        optionStage.draw();
    }

    @Override
    public void show() {
        //Gdx.gl.glClearColor(0, 0, 1, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.input.setInputProcessor(optionStage);
    }

    @Override
    public void dispose() {

        batchs.dispose();
        imgs.dispose();

    }
}
