package com.mygdx.game;
import com.badlogic.gdx.math.Vector2;
// Basically variables that wont change, such as TiledMap IDs
public final class Constants {

    // Filters for TiledMap (not used for current)
    public static final short BIT_WALL = 1;
    public static final short BIT_PLAYER = 2;
    public static final short BIT_SENSOR = 4;
    public static final short BIT_NOLIGHT = 8;
    public static final short BIT_BREAKABLE = 16;

    public static final float PPM = 50;//50.0f? //PPM Stands for Pixels Per Meter, this means 32 pixels are equal to a meter

    public static final Vector2 GRAVITY = new Vector2(0, 0);
    public static final float DEFAULT_ZOOM = 6f;

    public static final Vector2 RESOLUTION = new Vector2(640, 480);

    public static final int VELOCITY_ITERATION = 6;
    public static final int POSITION_ITERATION = 2;

    public static String MAP_NAME = "level1.tmx";
}
