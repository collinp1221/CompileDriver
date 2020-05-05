package com.mygdx.game;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import static com.mygdx.game.Constants.MAP_NAME;

public class MapLoader implements Disposable {

    private static final String MAP_WALL = "BG2";
    private static final String MAP_PLAYER = "FG";
    private static final float OBJECT_DENSITY = 1f;
    private static final float PLAYER_DENSITY = 0.4f;


    private final World mWorld;
    private final TiledMap mMap;

    /*
     * Main MapLoader constructor
     * @param world {@link com.topdowncar.game.screens.PlayScreen#mWorld} used to control and add physics objects
     */
    public MapLoader(World world) {
        this.mWorld = world;
        mMap = new TmxMapLoader().load(MAP_NAME);

        final Array<RectangleMapObject> walls = mMap.getLayers().get(MAP_WALL).getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject rObject : new Array.ArrayIterator<RectangleMapObject>(walls)) {
            Rectangle rectangle = rObject.getRectangle();
            ShapeFactory.createRectangle(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2), // position
                    new Vector2(rectangle.getWidth() / 2, rectangle.getHeight() / 2), // size
                    BodyDef.BodyType.StaticBody, mWorld, OBJECT_DENSITY, false);
        }
    }

    /*
     * Return player main rectangle that is used in
     * {@link com.topdowncar.game.entities.Car#Car(float, float, float, MapLoader, int, World)}
     * to position the player correctly
     * @return player rectangle received from map
     */
    public Body getPlayer() { // MAP_PLAYER use to replace foreground
        final Rectangle rectangle = new Rectangle();
        rectangle.set(5000.0f,5000.0f, 1000.0f, 3000.0f);
        System.out.println("getPlayer method, from Car");
        //final Rectangle rectangle = mMap.getLayers().get(MAP_PLAYER).getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        return ShapeFactory.createRectangle(
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2), // position
                new Vector2(rectangle.getWidth() / 2, rectangle.getHeight() / 2), // size
                BodyDef.BodyType.DynamicBody, mWorld, PLAYER_DENSITY, false);
    }


    @Override
    public void dispose() {
        mMap.dispose();
    }
}
