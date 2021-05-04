package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.time.*;

import static com.mygdx.game.Car.*;
import static com.mygdx.game.Car.TURN_DIRECTION_NONE;
import static com.mygdx.game.Constants.GRAVITY;

public class MainScreen extends ScreenAdapter implements InputProcessor
{

    private SpriteBatch batch; //SpriteBatch that stores all sprites to be used
    private MyGdxGame game;

    //TODO is this needed??
    private Texture carImg; //Player 1 car sprite

    private OrthographicCamera camera; //"Camera" Object that can be moved. This is what we see the game window through
    private double cameraZoom = 1.0;

    private TiledMap tiledMap; //TileMap data, taken from the .tmx file in /core/assets
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    //Various Tile Layers of the TileMap
    private TiledMapTileLayer backgroundLayer1; //Lowest layer. Below everything else
    private TiledMapTileLayer backgroundLayer2; //Below car
    private TiledMapTileLayer foregroundLayer; //Above car, no hitbox
    private TiledMapTileLayer foregroundLayer2; //Above car, hitbox
    private TiledMapTileLayer abovegroundLayer; //Above all other layers, no hitbox
    private MapLayer waypointLayer;
    private MapLayer startPosLayer;

    private MapObjects objects;

    private TiledMapTileSet tileset;

    public int tickCount = 0; //Counts the number of ticks that have occurred
    public int playerCount = 4; //Tracks the number of players in the game
    private String workingDirectory = System.getProperty("user.dir");
    private String absoluteFilePath = workingDirectory + File.separator + "ai" + File.separator + "defaultAI.txt";
    private String tileMapName;
    private final MapLoader mMapLoader;
    private final World mWorld;

    //Initialize textures and sprites
    private Texture carOverlay;
    private Sprite carOverlaySprite;
    private Texture player1Texture;
    private Sprite player1Sprite;
    private Texture player2Texture;
    private Sprite player2Sprite;
    private Texture player3Texture;
    private Sprite player3Sprite;
    private Texture player4Texture;
    private Sprite player4Sprite;
    private Texture tireTexture;
    private Sprite[] tireSprites;
    private BitmapFont lapFont;
    private BitmapFont playerFont;
    private BitmapFont lapTimeFont;

    //Variables to track current lap and total laps
    private int lapCount = 3;
    private int p1Lap = 1;
    private int p2Lap = 1;
    private int p3Lap = 1;
    private int p4Lap = 1;

    //Variables to track car waypoint position
    private int p1Pos = 0;
    private int p2Pos = 0;
    private int p3Pos = 0;
    private int p4Pos = 0;

    //Highest Waypoint Position
    private int p1HPos = 0;
    private int p2HPos = 0;
    private int p3HPos = 0;
    private int p4HPos = 0;

    //Store time of start of current lap for calculating lap time
    private Instant p1Start;
    private Instant p2Start;
    private Instant p3Start;
    private Instant p4Start;

    //Variable to store current lap time
    private String p1LapTime = "";
    private String p2LapTime = "";
    private String p3LapTime = "";
    private String p4LapTime = "";

    //Variable to store total time
    private int p1TotalTime = 0;
    private int p2TotalTime = 0;
    private int p3TotalTime = 0;
    private int p4TotalTime = 0;

    //Variables to store player spawn positions
    private float p1SpawnX = 0;
    private float p1SpawnY = 0;
    private float p1spawnAngle = 0;
    private float p2SpawnX = 0;
    private float p2SpawnY = 0;
    private float p2spawnAngle = 0;
    private float p3SpawnX = 0;
    private float p3SpawnY = 0;
    private float p3spawnAngle = 0;
    private float p4SpawnX = 0;
    private float p4SpawnY = 0;
    private float p4spawnAngle = 0;

    //Data for game over pop-up
    private boolean gameOver = false;
    private int winner = 0;



    //Body object for use in creating Box2D hitboxes
    private Body body;

    private final Box2DDebugRenderer mB2dr;//new

    private File TMXFile;

    //Car object variables for each player
    Car player1;
    Car player2;
    Car player3;
    Car player4;

    private Clock clock;

    //TODO Rework the way custom levels work so this can be removed
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
        try
        {
            player1 = new Car(1000000.0f, 1.7f, 100000, mMapLoader, Car.DRIVE_2WD, mWorld, absoluteFilePath, tiledMap);//= new Car(1, absoluteFilePath, 0, 50, 50, tiledMap);
            player2 = new Car(1000000.0f, 0.7f, 100000, mMapLoader, Car.DRIVE_2WD, mWorld, absoluteFilePath, tiledMap);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        //player1.setSprite(carImg); //Sets the sprite for player 1
        //player2.setSprite(carImg); //Sets the sprite for player 1

        //TODO Implement more layers/less layers (Cycle 2??)
        //Assign the proper layer ID to each TileMap Layer
        backgroundLayer1 = (TiledMapTileLayer)mapLayers.get(0);
        backgroundLayer2 = (TiledMapTileLayer)mapLayers.get(1);
        foregroundLayer = (TiledMapTileLayer)mapLayers.get(2);
        //collisionLayer = mapLayers.get(3);

        //objects = collisionLayer.getObjects();

        //Gdx.graphics.setWindowedMode(tiledMap.getProperties().get("width",Integer.class) * 32, tiledMap.getProperties().get("height",Integer.class) * 32);
        //TODO If the window is over a certain size, shrink it


        Gdx.input.setInputProcessor(this);


    }


    //Preset Levels (currently, at least. Rework in the future to make it so custom levels use this constructor)
    public MainScreen(MyGdxGame agame, String levelName) throws FileNotFoundException //TODO Add in an object to store TMX File here
    {

        game = new MyGdxGame();
        game = agame;

        //Initialize Fonts to be used
        FreeTypeFontGenerator fgen = new FreeTypeFontGenerator(Gdx.files.internal("Fonts\\Retro Gaming.ttf"));
        //Lap Count font
        FreeTypeFontGenerator.FreeTypeFontParameter parameter1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter1.size = 26;
        parameter1.color.set(Color.BLACK);
        lapFont = fgen.generateFont(parameter1);

        //Player 1/2/3/4 Text
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 30;
        parameter2.color.set(Color.BLACK);
        playerFont = fgen.generateFont(parameter2);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter3.size = 18;
        parameter3.color.set(Color.BLACK);
        lapTimeFont = fgen.generateFont(parameter3);



        fgen.dispose();

        //testFont = new BitmapFont(fs.internal("Fonts\\Retro Gaming.ttf"));
        //testFont.setColor(Color.BLACK);

        mB2dr = new Box2DDebugRenderer(); // new
        tileMapName = levelName;

        batch = new SpriteBatch();
        carImg = new Texture("car1.png");

        //Overlay that goes at the bottom of the screen (displays laps, time, place, etc.)
        carOverlay = new Texture("carOverlay.png");
        carOverlaySprite = new  Sprite(carOverlay);

        player1Texture = new Texture("car1.png");
        player1Sprite = new Sprite(player1Texture);
        if(playerCount >= 2)
        {
            player2Texture = new Texture("car2.png");
            player2Sprite = new Sprite(player2Texture);
        }
        if(playerCount >= 3)
        {
            player3Texture = new Texture("car3.png");
            player3Sprite = new Sprite(player3Texture);
        }
        if(playerCount >= 4)
        {
            player4Texture = new Texture("car4.png");
            player4Sprite = new Sprite(player4Texture);
        }

        tireTexture = new Texture("tire.png");
        tireSprites = new Sprite[16];

        for(int i = 0; i < 16; i++)
        {
            tireSprites[i] = new Sprite(tireTexture);
        }

        //Camera setup
        camera = new OrthographicCamera();
        cameraZoom = .75;
        camera.setToOrtho(false, (float)(Gdx.graphics.getWidth() * cameraZoom), (float)((Gdx.graphics.getHeight()) * cameraZoom));
        camera.update();

        //Set up world
        mWorld = new World(GRAVITY, true);
        mMapLoader = new MapLoader(mWorld);


        //Create contact listener to check for collisions between Body objects
        mWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact)
            {
                //Car collides with sensor hitbox:
                if(contact.getFixtureA().getBody().getUserData() == player1.getBody().getUserData() && contact.getFixtureB().isSensor())
                {
                    //Update car's waypoint upon collision with waypoint hitbox
                    if(contact.getFixtureA().getBody().getUserData() == player1.getBody().getUserData() && contact.getFixtureB().getBody().getUserData() != null)
                    {
                        p1Pos = (Integer) contact.getFixtureB().getBody().getUserData();
                        if(p1Pos == p1HPos + 1 || p1Pos == p1HPos - 1)
                            p1HPos = p1Pos;
                    }
                    if(playerCount >= 2)
                    {
                        if (contact.getFixtureA().getBody().getUserData() == player2.getBody().getUserData() && contact.getFixtureB().getBody().getUserData() != null)
                        {
                            p2Pos = (Integer) contact.getFixtureB().getBody().getUserData();
                            if(p2Pos == p2HPos + 1 || p2Pos == p2HPos - 1)
                                p2HPos = p2Pos;
                        }
                    }
                    if(playerCount >= 3) {
                        if (contact.getFixtureA().getBody().getUserData() == player3.getBody().getUserData() && contact.getFixtureB().getBody().getUserData() != null)
                        {
                            p3Pos = (Integer) contact.getFixtureB().getBody().getUserData();
                            if(p3Pos == p3HPos + 1 || p3Pos == p3HPos - 1)
                                p3HPos = p3Pos;
                        }
                    }
                    if(playerCount >= 4)
                    {
                        if (contact.getFixtureA().getBody().getUserData() == player4.getBody().getUserData() && contact.getFixtureB().getBody().getUserData() != null)
                        {
                            p4Pos = (Integer) contact.getFixtureB().getBody().getUserData();
                            if(p4Pos == p4HPos + 1 || p4Pos == p4HPos - 1)
                                p4HPos = p4Pos;
                        }
                    }

                    //If a car completes a full lap, add a lap, and reset position/highest position and starting lap time
                    if(p1Pos == 0 && p1HPos == waypointLayer.getObjects().getCount() - 1)
                    {
                        p1HPos = 0;
                        p1Lap ++;
                        p1TotalTime += (int)Duration.between(p1Start, Instant.now()).toMillis();
                        p1Start = Instant.now();
                    }
                    if(p2Pos == 0 && p2HPos == waypointLayer.getObjects().getCount() - 1)
                    {
                        p2HPos = 0;
                        p2Lap ++;
                        p2TotalTime += (int)Duration.between(p2Start, Instant.now()).toMillis();
                        p2Start = Instant.now();
                    }
                    if(p3Pos == 0 && p3HPos == waypointLayer.getObjects().getCount() - 1)
                    {
                        p3HPos = 0;
                        p3Lap ++;
                        p3TotalTime += (int)Duration.between(p3Start, Instant.now()).toMillis();
                        p3Start = Instant.now();
                    }
                    if(p4Pos == 0 && p4HPos == waypointLayer.getObjects().getCount() - 1)
                    {
                        p4HPos = 0;
                        p4Lap ++;
                        p4TotalTime += (int)Duration.between(p4Start, Instant.now()).toMillis();
                        p4Start = Instant.now();
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        //Load TileMap file (.tmx)
        tiledMap = new TmxMapLoader().load(tileMapName);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        MapLayers mapLayers = tiledMap.getLayers();

        //Assign the proper layer ID to each TileMap Layer
        backgroundLayer1 = (TiledMapTileLayer)mapLayers.get(0);
        backgroundLayer2 = (TiledMapTileLayer)mapLayers.get(1);
        foregroundLayer = (TiledMapTileLayer)mapLayers.get(2);
        abovegroundLayer = (TiledMapTileLayer)mapLayers.get(3);
        waypointLayer = mapLayers.get(4);
        startPosLayer = mapLayers.get(5);

        //Get player spawns from TileMap StartPoint layer
        for(int i = 0; i < startPosLayer.getObjects().getCount(); i++)
        {
            if((int)startPosLayer.getObjects().get(i).getProperties().get("player") == 1)
            {
                RectangleMapObject spawnObj = (RectangleMapObject)startPosLayer.getObjects().get(i);
                p1SpawnX = spawnObj.getRectangle().x + spawnObj.getRectangle().width / 2;
                p1SpawnY = spawnObj.getRectangle().y + spawnObj.getRectangle().height / 2;
                p1spawnAngle = (-90 * (3.14159f / 180)) + ((int)startPosLayer.getObjects().get(i).getProperties().get("angle") * (3.14159f / 180));
            }
            else if((int)startPosLayer.getObjects().get(i).getProperties().get("player") == 2 && playerCount >= 2)
            {
                RectangleMapObject spawnObj = (RectangleMapObject)startPosLayer.getObjects().get(i);
                p2SpawnX = spawnObj.getRectangle().x;
                p2SpawnY = spawnObj.getRectangle().y;
                p2spawnAngle = (-90 * (3.14159f / 180)) + ((int)startPosLayer.getObjects().get(i).getProperties().get("angle") * (3.14159f / 180));
            }
            else if((int)startPosLayer.getObjects().get(i).getProperties().get("player") == 3 && playerCount >= 3)
            {
                RectangleMapObject spawnObj = (RectangleMapObject)startPosLayer.getObjects().get(i);
                p3SpawnX = spawnObj.getRectangle().x;
                p3SpawnY = spawnObj.getRectangle().y;
                p3spawnAngle = (-90 * (3.14159f / 180)) + ((int)startPosLayer.getObjects().get(i).getProperties().get("angle") * (3.14159f / 180));
            }
            else if((int)startPosLayer.getObjects().get(i).getProperties().get("player") == 4 && playerCount >= 4)
            {
                RectangleMapObject spawnObj = (RectangleMapObject)startPosLayer.getObjects().get(i);
                p4SpawnX = spawnObj.getRectangle().x;
                p4SpawnY = spawnObj.getRectangle().y;
                p4spawnAngle = (-90 * (3.14159f / 180)) + ((int)startPosLayer.getObjects().get(i).getProperties().get("angle") * (3.14159f / 180));
            }
        }

        //Initialize players
        try
        {
            player1 = new Car(1000000.0f, 0.45f, 100000, mMapLoader, Car.DRIVE_2WD, mWorld, absoluteFilePath, tiledMap);//= new Car(1, absoluteFilePath, 0, 50, 50, tiledMap);
            if(playerCount >= 2)
                player2 = new Car(1000000.0f, 0.45f, 100000, mMapLoader, Car.DRIVE_2WD, mWorld, absoluteFilePath, tiledMap);
            if(playerCount >= 3)
                player3 = new Car(1000000.0f, 0.45f, 100000, mMapLoader, Car.DRIVE_2WD, mWorld, absoluteFilePath, tiledMap);
            if(playerCount >= 4)
                player4 = new Car(1000000.0f, 0.45f, 100000, mMapLoader, Car.DRIVE_2WD, mWorld, absoluteFilePath, tiledMap);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        //Set player sprites
        //player1.setSprite(carImg); //Sets the sprite for player 1
        //player2.setSprite(carImg); //Sets the sprite for player 1

        player1.getBody().setUserData(player1Sprite);
        if(playerCount >= 2)
        {
            player2.getBody().setUserData(player2Sprite);
        }
        if(playerCount >= 3)
        {
            player3.getBody().setUserData(player3Sprite);
        }
        if(playerCount >= 4)
        {
            player4.getBody().setUserData(player4Sprite);
        }

        //Move players to spawn position
        player1.getBody().setTransform(p1SpawnX, p1SpawnY, p1spawnAngle);
        for(int i = 0; i < 4; i++)
            player1.getBody().getJointList().get(i).joint.getBodyA().setTransform(p1SpawnX, p1SpawnY, p1spawnAngle);
        for(int i = 0; i < 4; i++)
            player1.getBody().getJointList().get(i).joint.getBodyB().setTransform(p1SpawnX, p1SpawnY, p1spawnAngle);

        if(playerCount >= 2)
        {
            player2.getBody().setTransform(p1SpawnX, p1SpawnY, p2spawnAngle);
            for(int i = 0; i < 4; i++)
                player2.getBody().getJointList().get(i).joint.getBodyA().setTransform(p2SpawnX, p2SpawnY, p2spawnAngle);
            for(int i = 0; i < 4; i++)
                player2.getBody().getJointList().get(i).joint.getBodyB().setTransform(p2SpawnX, p2SpawnY, p2spawnAngle);
        }
        if(playerCount >= 3)
        {
            player3.getBody().setTransform(p3SpawnX, p3SpawnY, p3spawnAngle);
            for(int i = 0; i < 4; i++)
                player3.getBody().getJointList().get(i).joint.getBodyA().setTransform(p3SpawnX, p3SpawnY, p3spawnAngle);
            for(int i = 0; i < 4; i++)
                player3.getBody().getJointList().get(i).joint.getBodyB().setTransform(p3SpawnX, p3SpawnY, p3spawnAngle);
        }
        if(playerCount >= 4)
        {
            player4.getBody().setTransform(p4SpawnX, p4SpawnY, p4spawnAngle);
            for(int i = 0; i < 4; i++)
                player4.getBody().getJointList().get(i).joint.getBodyA().setTransform(p4SpawnX, p4SpawnY, p4spawnAngle);
            for(int i = 0; i < 4; i++)
                player4.getBody().getJointList().get(i).joint.getBodyB().setTransform(p4SpawnX, p4SpawnY, p4spawnAngle);
        }

        camera.position.set(player1.getBody().getPosition().x, player1.getBody().getPosition().y, 0);

        BodyDef bd = new BodyDef();

        //Add hitboxes for each element in background layer 2
        System.out.println("Initializing Collision Hitboxes");
        for(int i = 0; i < foregroundLayer.getHeight(); i++)
        {
            for(int j = 0; j < foregroundLayer.getWidth(); j++)
            {
                //If cell ID can be found with no errors, that means there is a tile there
                try
                {
                    foregroundLayer.getCell(j, i).getTile().getId();
                    System.out.println("Success at (" + j + ", " + i + "): Creating hitbox");

                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody;
                    bodyDef.position.set(0, 0);

                    body = mWorld.createBody(bodyDef);

                    PolygonShape testShape = new PolygonShape();
                    testShape.setAsBox(16, 16, new Vector2(32 * j + 16, 32 * i + 16), 0);

                    FixtureDef fd = new FixtureDef();
                    fd.shape = testShape;
                    body.createFixture(fd);
                    testShape.dispose();
                }
                catch (Exception e)
                {

                }
            }
        }
        //Create collision box at bottom of map
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);
        body = mWorld.createBody(bodyDef);
        PolygonShape testShape = new PolygonShape();
        testShape.setAsBox(backgroundLayer1.getWidth() * 32, 10, new Vector2(0, -10), 0);
        FixtureDef fd = new FixtureDef();
        fd.shape = testShape;
        body.createFixture(fd);
        testShape.dispose();

        //Create collision box at top of map
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);
        body = mWorld.createBody(bodyDef);
        testShape = new PolygonShape();
        testShape.setAsBox(backgroundLayer1.getWidth() * 32, 10, new Vector2(0, backgroundLayer1.getHeight() * 32 + 16), 0);
        fd = new FixtureDef();
        fd.shape = testShape;
        body.createFixture(fd);
        testShape.dispose();

        //Create collision box at left side of map
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);
        body = mWorld.createBody(bodyDef);
        testShape = new PolygonShape();
        testShape.setAsBox(10, backgroundLayer1.getHeight() * 32, new Vector2(-10, 0), 0);
        fd = new FixtureDef();
        fd.shape = testShape;
        body.createFixture(fd);
        testShape.dispose();

        //Create collision box at right side of map
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);
        body = mWorld.createBody(bodyDef);
        testShape = new PolygonShape();
        testShape.setAsBox(10, backgroundLayer1.getHeight() * 32, new Vector2(backgroundLayer1.getWidth() * 32 + 16, 0), 0);
        fd = new FixtureDef();
        fd.shape = testShape;
        body.createFixture(fd);
        testShape.dispose();

        //Create sensor hitboxes for waypoint detection
        System.out.println("Initializing Waypoints:");
        for(int i = 0; i < waypointLayer.getObjects().getCount(); i++)
        {
            RectangleMapObject test = (RectangleMapObject)waypointLayer.getObjects().get(i);
            System.out.println("x: " + test.getRectangle().x + "y: " + test.getRectangle().y);

            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(0, 0);

            body = mWorld.createBody(bodyDef);
            body.setUserData(waypointLayer.getObjects().get(i).getProperties().get("id"));

            testShape = new PolygonShape();
            float xPos = test.getRectangle().x + (test.getRectangle().width / 2);
            float yPos = test.getRectangle().y + (test.getRectangle().height / 2);
            testShape.setAsBox(test.getRectangle().width / 2, test.getRectangle().height / 2, new Vector2(xPos, yPos), 0);

            fd = new FixtureDef();
            fd.isSensor = true;
            fd.shape = testShape;
            body.createFixture(fd);
            testShape.dispose();

        }



        Gdx.input.setInputProcessor(this);


        //Set player lap start times
        int startMin = LocalTime.now().getMinute();
        int startSec = LocalTime.now().getSecond();


        //Set start time for all player lap timers
        Instant startTime = Instant.now();
        p1Start = startTime;
        p2Start = startTime;
        p3Start = startTime;
        p4Start = startTime;


    }

    private void handleInput()
    { // TODO Modify to be applicable for multiple cars, if not already
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

    private void update(final float delta)
    {
        player1.update(delta);
        player2.update(delta);

        //camera.position.set(player1.getBody().getPosition(), 0);
        camera.update();
        mWorld.step(delta, 6, 2);
    }

    //Render loop. This will execute constantly while MainScreen is the current screen
    @Override
    public void render(float delta)
    {
        //When a player has completed all of their laps, end the race
        if(p1Lap > lapCount && !gameOver)
            {
                winner = 1;
                gameOver = true;
            }
        else if(p2Lap > lapCount && !gameOver)
        {
            winner = 2;
            gameOver = true;
        }
        else if(p3Lap > lapCount && !gameOver)
        {
            winner = 3;
            gameOver = true;
        }
        else if(p4Lap > lapCount && !gameOver)
        {
            winner = 4;
            gameOver = true;
        }

        //Sets the background to be black (just in case of issues drawing the background)
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for(int i = 0; i < 4; i++)
        {
            try {
                int xPos = (int) (player1.mAllWheels.get(i).getBody().getPosition().x / 32);
                int yPos = (int) (player1.mAllWheels.get(i).getBody().getPosition().y / 32);
                if (backgroundLayer1.getCell(xPos, yPos).getTile().getProperties().get("tileType").equals("grass"))
                {
                    //TODO make this actually work
                    //player1.mAllWheels.get(i).getBody().applyForceToCenter(player1.getBody().getLinearVelocity().x * -.75f, player1.getBody().getLinearVelocity().y * -.75f, true);
                }
            }
            catch(Exception e)
            {
            }
        }




        //Update player 1 car sprite position and angle
        float p1Xpos = 335f - ((camera.position.x - player1.getBody().getPosition().x) * 1.3333333f);
        float p1Ypos = 360f - ((camera.position.y - player1.getBody().getPosition().y) * 1.3333333f);

        player1Sprite.setRotation(player1.getBody().getAngle() * 57.295779513f); //Converts from radians to degrees
        player1Sprite.setPosition(p1Xpos, p1Ypos);

        float p1tire1Xpos = 335f - ((camera.position.x - player1.getBody().getFixtureList().get(0).getBody().getPosition().x) * 1.3333333f);
        float p1tire1Ypos = 360f - ((camera.position.y - player1.getBody().getFixtureList().get(0).getBody().getPosition().y) * 1.3333333f);

        tireSprites[0].setPosition(p1tire1Xpos, p1tire1Ypos);

        if(playerCount >= 2)
        {
            //Set player 2 sprite position based on camera position and player 2's x and y coordinates
            float p2xPos = 335f - ((camera.position.x - player2.getBody().getPosition().x) * 1.3333333f);
            float p2yPos = 360f - ((camera.position.y - player2.getBody().getPosition().y) * 1.3333333f);
            player2Sprite.setRotation(player2.getBody().getAngle() * 57.295779513f);
            player2Sprite.setPosition(p2xPos, p2yPos);
        }
        if(playerCount >= 3)
        {
            //Set player 2 sprite position based on camera position and player 2's x and y coordinates
            float p3xPos = 335f - ((camera.position.x - player3.getBody().getPosition().x) * 1.3333333f);
            float p3yPos = 360f - ((camera.position.y - player3.getBody().getPosition().y) * 1.3333333f);
            player3Sprite.setRotation(player3.getBody().getAngle() * 57.295779513f);
            player3Sprite.setPosition(p3xPos, p3yPos);
        }
        if(playerCount >= 4)
        {
            //Set player 4 sprite position based on camera position and player 4's x and y coordinates
            float p4xPos = 335f - ((camera.position.x - player4.getBody().getPosition().x) * 1.3333333f);
            float p4yPos = 360f - ((camera.position.y - player4.getBody().getPosition().y) * 1.3333333f);
            player4Sprite.setRotation(player4.getBody().getAngle() * 57.295779513f);
            player4Sprite.setPosition(p4xPos, p4yPos);
        }

        //Translate camera to stay on top of player 1's car
        camera.update();
        tiledMapRenderer.setView(camera);

        //Translate camera to keep it on top of car's position
        camera.position.set(player1.getBody().getPosition().x, player1.getBody().getPosition().y - 75, 0);

        if(camera.position.y > (foregroundLayer.getHeight() * 32) - (400 * cameraZoom))
            camera.position.set(camera.position.x, (float)((foregroundLayer.getHeight() * 32) - (400 * cameraZoom)), 0);
        if(camera.position.y < (250 * cameraZoom))
            camera.position.set(camera.position.x, (float)(250 * cameraZoom), 0);
        if(camera.position.x > (foregroundLayer.getWidth() * 32) - (350 * cameraZoom))
            camera.position.set((float)((foregroundLayer.getWidth() * 32) - (350 * cameraZoom)), camera.position.y, 0);
        if(camera.position.x < 0 + (350 * cameraZoom))
            camera.position.set((float)(350 * cameraZoom), camera.position.y, 0);




        //int[] backgroundLayers = {0,1}; //Set the background layer as the two bottom-most layers
        //int[] foregroundLayer = {2}; //Set the foreground layer as the third-lowest layer

        tiledMapRenderer.getBatch().begin();
        tiledMapRenderer.renderTileLayer(backgroundLayer1);
        tiledMapRenderer.renderTileLayer(backgroundLayer2);
        tiledMapRenderer.getBatch().end();


        //Render debug textures
        mB2dr.render(mWorld, camera.combined);


        //Does a single gametick for player 1's car
        //player1.doTick();
        handleInput();

        //Update lap times for players

        int seconds = (int)Duration.between(p1Start, Instant.now()).toMillis() / 1000;
        p1LapTime = "";
        if((seconds / 60) < 10)
            p1LapTime = p1LapTime.concat("0");
        p1LapTime = p1LapTime.concat((seconds / 60) + ":");
        if((seconds % 60) < 10)
            p1LapTime = p1LapTime.concat("0");
        p1LapTime = p1LapTime.concat("" + (seconds % 60));
        if(seconds >= 5940)
            p1LapTime = "99:00";

        if(playerCount >= 2)
        {
            seconds = (int)Duration.between(p2Start, Instant.now()).toMillis() / 1000;
            p2LapTime = "";
            if((seconds / 60) < 10)
                p2LapTime = p2LapTime.concat("0");
            p2LapTime = p2LapTime.concat((seconds / 60) + ":");
            if((seconds % 60) < 10)
                p2LapTime = p2LapTime.concat("0");
            p2LapTime = p2LapTime.concat("" + (seconds % 60));
            if(seconds >= 5940)
                p2LapTime = "99:00";
        }
        if(playerCount >= 3)
        {
            seconds = (int)Duration.between(p3Start, Instant.now()).toMillis() / 1000;
            p3LapTime = "";
            if((seconds / 60) < 10)
                p3LapTime = p3LapTime.concat("0");
            p3LapTime = p3LapTime.concat((seconds / 60) + ":");
            if((seconds % 60) < 10)
                p3LapTime = p3LapTime.concat("0");
            p3LapTime = p3LapTime.concat("" + (seconds % 60));
            if(seconds >= 5940)
                p3LapTime = "99:00";
        }
        if(playerCount >= 4)
        {
            seconds = (int)Duration.between(p4Start, Instant.now()).toMillis() / 1000;
            p4LapTime = "";
            if((seconds / 60) < 10)
                p4LapTime = p4LapTime.concat("0");
            p4LapTime = p4LapTime.concat((seconds / 60) + ":");
            if((seconds % 60) < 10)
                p4LapTime = p4LapTime.concat("0");
            p4LapTime = p4LapTime.concat("" + (seconds % 60));
            if(seconds >= 5940)
                p4LapTime = "99:00";
        }

        //DRAW SPRITES
        batch.begin();
        player1Sprite.draw(batch);
        if(playerCount >= 2)
            player2Sprite.draw(batch);
        if(playerCount >= 3)
            player3Sprite.draw(batch);
        if(playerCount >= 4)
            player4Sprite.draw(batch);
        batch.end();

        //TODO Implement more layers
        tiledMapRenderer.getBatch().begin();
        tiledMapRenderer.renderTileLayer(foregroundLayer);
        tiledMapRenderer.renderTileLayer(abovegroundLayer);
        //tiledMapRenderer.renderObjects(collisionLayer); //TODO Potentially implement this somehow???
        tiledMapRenderer.getBatch().end();

        //Render HUD overlay
        batch.begin();
        carOverlaySprite.draw(batch);
        playerFont.draw(batch, "Player 1", 10, 140); //p1 label
        if(playerCount >= 2)
            playerFont.draw(batch, "Player 2", 185, 140); //p2 label
        if(playerCount >= 3)
            playerFont.draw(batch, "Player 3", 360, 140); //p3 label
        if(playerCount >= 4)
            playerFont.draw(batch, "Player 4", 535, 140); //p4 label

        lapFont.draw(batch, "LAP: " + p1Lap + " / " + lapCount, 12, 95);
        if(playerCount >= 2)
            lapFont.draw(batch, "LAP: " + p2Lap + " / " + lapCount, 187, 95);
        if(playerCount >= 3)
            lapFont.draw(batch, "LAP: " + p3Lap + " / " + lapCount, 362, 95);
        if(playerCount >= 4)
            lapFont.draw(batch, "LAP: " + p4Lap + " / " + lapCount, 537, 95);

        //Render Lap Time text
        lapTimeFont.draw(batch, "Lap Time: ", 35, 65); //p1 lap time
        if(playerCount >= 2)
            lapTimeFont.draw(batch, "Lap Time: ", 210, 65); //p2 lap time
        if(playerCount >= 3)
            lapTimeFont.draw(batch, "Lap Time: ", 385, 65); //p3 lap time
        if(playerCount >= 4)
            lapTimeFont.draw(batch, "Lap Time: ", 560, 65); //p4 lap time

        //Render actual lap times (ironically not using lap time font)
        lapFont.draw(batch, p1LapTime, 42, 40);
        lapFont.draw(batch, p2LapTime, 217, 40);
        lapFont.draw(batch, p3LapTime, 392, 40);
        lapFont.draw(batch, p4LapTime, 567, 40);

        //If game is over, draw 'Game Over' screen over everything else
        if(gameOver)
        {
            try
            {
                game.setScreen(new GameOver(game, winner, tileMapName));
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
        }
        batch.end();


        //Run physics calculations for hitboxes and rigidbodies
        update(delta);
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
    public boolean keyUp(int keycode)
    {


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

}