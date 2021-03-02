package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;//new
import com.badlogic.gdx.physics.box2d.World;//new
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.io.File;
import java.io.FileNotFoundException;

import static com.mygdx.game.Car.*;
import static com.mygdx.game.Car.TURN_DIRECTION_NONE;
import static com.mygdx.game.Constants.GRAVITY;

public class MainScreen extends ScreenAdapter implements InputProcessor {

    private SpriteBatch batch; //SpriteBatch that stores all sprites to be used
    private Texture carImg; //Player 1 car sprite

    private OrthographicCamera camera; //"Camera" Object that can be moved. This is what we see the game window through

    private TiledMap tiledMap; //TileMap data, taken from the .tmx file in /core/assets
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
    private String tileMapName;
    private final MapLoader mMapLoader;
    private final World mWorld;
    private final Box2DDebugRenderer mB2dr;//new

    private File TMXFile;

    public boolean hasScaled = false;

    Car player1;
    //Car player2 = new Car(2);
    //Car player3 = new Car(3);
    //Car player4 = new Car(4);

    public MainScreen(MyGdxGame agame, File tileMapFile)
    {
        mB2dr = new Box2DDebugRenderer(); // new
        TMXFile = tileMapFile;

        batch = new SpriteBatch();
        carImg = new Texture("car1.png");

        //Camera setup
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //camera.translate(0,0);
        camera.update();
        //new, TODO document new
        mWorld = new World(GRAVITY, true);
        mMapLoader = new MapLoader(mWorld);

        //TODO Throw an error and exit if the tilemap has a width or height < 18
        tiledMap = new TmxMapLoader().load(TMXFile.getAbsolutePath());
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        MapLayers mapLayers = tiledMap.getLayers();

        //Initialize players
        try {
            player1 = new Car(1000000.0f, 0.7f, 100000, mMapLoader, Car.DRIVE_2WD, mWorld, absoluteFilePath, tiledMap);//= new Car(1, absoluteFilePath, 0, 50, 50, tiledMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        player1.setSprite(carImg); //Sets the sprite for player 1

        //TODO Implement more layers/less layers (Cycle 2??)
        //Assign the proper layer ID to each TileMap Layer
        backgroundLayer1 = (TiledMapTileLayer)mapLayers.get(0);
        backgroundLayer2 = (TiledMapTileLayer)mapLayers.get(1);
        foregroundLayer = (TiledMapTileLayer)mapLayers.get(2);
        //collisionLayer = mapLayers.get(3);

        //objects = collisionLayer.getObjects();

        Gdx.graphics.setWindowedMode(tiledMap.getProperties().get("width",Integer.class) * 32, tiledMap.getProperties().get("height",Integer.class) * 32);
        //TODO If the window is over a certain size, shrink it


        Gdx.input.setInputProcessor(this);


        //TODO Setup players 2-4 if they are in the game (Dev Cycle 2)

    }

    public MainScreen(MyGdxGame agame, String levelName) throws FileNotFoundException //TODO Add in an object to store TMX File here
    {
        mB2dr = new Box2DDebugRenderer(); // new
        tileMapName = levelName;

        batch = new SpriteBatch();
        carImg = new Texture("car1.png");

        //Camera setup
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //camera.translate(0,0);
        camera.update();
        mWorld = new World(GRAVITY, true);
        mMapLoader = new MapLoader(mWorld);

        //TODO Throw an error and exit if the tilemap has a width or height < 18
        tiledMap = new TmxMapLoader().load(tileMapName);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        MapLayers mapLayers = tiledMap.getLayers();

        //Initialize players
        try {
            player1 = new Car(1000000.0f, 0.7f, 100000, mMapLoader, Car.DRIVE_2WD, mWorld, absoluteFilePath, tiledMap);//= new Car(1, absoluteFilePath, 0, 50, 50, tiledMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        player1.setSprite(carImg); //Sets the sprite for player 1

        //TODO Implement more layers/less layers (Cycle 2??)
        //Assign the proper layer ID to each TileMap Layer
        backgroundLayer1 = (TiledMapTileLayer)mapLayers.get(0);
        backgroundLayer2 = (TiledMapTileLayer)mapLayers.get(1);
        foregroundLayer = (TiledMapTileLayer)mapLayers.get(2);
        //collisionLayer = mapLayers.get(3);

        //objects = collisionLayer.getObjects();

        Gdx.graphics.setWindowedMode(tiledMap.getProperties().get("width",Integer.class) * 32, tiledMap.getProperties().get("height",Integer.class) * 32);
        //TODO If the window is over a certain size, shrink it


        Gdx.input.setInputProcessor(this);


        //TODO Setup players 2-4 if they are in the game (Dev Cycle 2?)
    }

    private void handleInput() { // TODO Modify to be applicable for multiple cars, if not already
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player1.setDriveDirection(DRIVE_DIRECTION_FORWARD);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player1.setDriveDirection(DRIVE_DIRECTION_BACKWARD);
        } else {
            player1.setDriveDirection(DRIVE_DIRECTION_NONE);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player1.setTurnDirection(TURN_DIRECTION_LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player1.setTurnDirection(TURN_DIRECTION_RIGHT);
        } else {
            player1.setTurnDirection(TURN_DIRECTION_NONE);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void update(final float delta) {
        player1.update(delta);

        //camera.position.set(player1.getBody().getPosition(), 0);
        camera.update();
        mWorld.step(delta, 6, 2);
    }

    @Override
    public void render(float delta){
        //Sets the background to be black (just in case of issues drawing the background)
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Update camera position
        update(delta);// temp, use bellow
        //Update camera position
       // camera.update();
        tiledMapRenderer.setView(camera);

        //int[] backgroundLayers = {0,1}; //Set the background layer as the two bottom-most layers
        //int[] foregroundLayer = {2}; //Set the foreground layer as the third-lowest layer

        tiledMapRenderer.getBatch().begin();
        tiledMapRenderer.renderTileLayer(backgroundLayer1);
        tiledMapRenderer.renderTileLayer(backgroundLayer2);
        tiledMapRenderer.getBatch().end();

        //tiledMapRenderer.render();

        //TODO USE THIS!!!!!
        //System.out.println(backgroundLayer1.getCell(0, 0).getTile().getProperties().get("tileType"));


        mB2dr.render(mWorld, camera.combined);
        //Does a single gametick for player 1's car
        //player1.doTick();
        handleInput();
        //Begins the SpriteBatch, to allow for drawing sprites to the screen
        batch.begin();

        //DRAW SPRITES
        //Draw the sprite of player 1's car
        //player1.getSprite().draw(batch);

        //Ends the SpriteBatch. After this, nothing can be drawn until the batch is started again
        batch.end();

        tiledMapRenderer.getBatch().begin();
        tiledMapRenderer.renderTileLayer(foregroundLayer);
        //tiledMapRenderer.renderObjects(collisionLayer); //TODO make this render hitboxes, or just work period honestly
        tiledMapRenderer.getBatch().end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    //For camera movement

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        /*
        if(keycode == Input.Keys.LEFT)
        {
            camera.translate(-32,0);
            player1.setXPos((int)player1.getXPos() + 32);
        }
        if(keycode == Input.Keys.RIGHT)
        {
            camera.translate(32,0);
            player1.setXPos((int)player1.getXPos() - 32);
        }
        if(keycode == Input.Keys.UP)
        {
            camera.translate(0,-32);
            player1.setYPos((int)player1.getYPos() + 32);
        }
        if(keycode == Input.Keys.DOWN)
        {
            camera.translate(0,32);
            player1.setYPos((int)player1.getYPos() - 32);
        }
        if(keycode == Input.Keys.NUM_1)
            tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
        if(keycode == Input.Keys.NUM_2)
            tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
        */

        return false;
    }

    //Overrides to allow for camera movement

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void resize(int width, int height) {
        if (!hasScaled) {
            hasScaled = true;

            camera.viewportWidth = 32 * tiledMap.getProperties().get("width", Integer.class);
            camera.viewportHeight = 32 * tiledMap.getProperties().get("height", Integer.class);

            camera.translate(13, 13); //Translate the camera so that the track does not fall off the side of the screen

            //Formulas for translating screen. I don't entirely understand how they work, so please don't mess with them!
            //Translate camera properly if width > 18
            if (tiledMap.getProperties().get("width", Integer.class) > 18) {
                int amount = tiledMap.getProperties().get("width", Integer.class) - 18;
                if (amount % 2 == 1) //If # is odd
                    camera.translate(16, 0);
                amount = amount / 2;
                amount = amount * 32;
                camera.translate(amount, 0);
            }
            //Translate camera properly if height > 18
            if (tiledMap.getProperties().get("height", Integer.class) > 18) {
                System.out.println("Translating camera (height > 18)");
                int amount = tiledMap.getProperties().get("height", Integer.class) - 18;
                if (amount % 2 == 1) //if # is odd
                    camera.translate(0, 16);
                amount = amount / 2;
                amount = amount * 32;
                camera.translate(0, amount);
            }


            //Scale player sprite size, to maintain consistent size despite camera zoom
            //player1.getSprite().setScale(750f / Gdx.graphics.getWidth(), 750f / Gdx.graphics.getHeight());

            //Update the camera to reflect all changes made
            camera.update();
        }

    }
}