package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Car{

    //Negative moves left, positive moves right
    private double XVelocity = 0;

    //Negative moves down, positive moves up
    private double YVelocity = 0;

    //X Position of the car
    private double XPos = 0;

    //Y Position of the car
    private double YPos = 0;

    //Tracks the angle of the car. 0 is facing right, 90 is facing up, 180 is facing left, etc.
    private float angle = 0;

    //Maximum X value that the Car can travel to.
    private float maxX = 550;

    //Maximum Y value that the Car can travel to.
    private float maxY = 550;

    //TODO Implement this. Probably do this in 1st design cycle?
    //Tracks the most recent flag passed. Starts at 0 (Start/Finish Line)
    private int currentFlag = 0;

    //Tracks what player the car is
    private int playerNumber;

    //Sprite of the car
    private Sprite sprite;

    //VM Class, used to read and interpret the user's assembly file
    private VM vm;

    //TODO Implement
    private TiledMap tiledMap;

    private int currentTileX;

    private int currentTileY;

    private int adjustedTileX = 0;

    private int adjustedTileY = 0;

    //Constructor Classes

    //Default constructor with only player number, uses the default AI
    public Car(int player) throws FileNotFoundException {
        playerNumber = player;

        String workingDirectory = System.getProperty("user.dir");
        String absoluteFilePath = workingDirectory + File.separator + "ai" + File.separator + "defaultAI.txt";

        vm = new VM(absoluteFilePath);
    }

    //Constructor class with custom AI
    public Car(int player, String filePath, int startingAngle, int startingX, int startingY, TiledMap map) throws FileNotFoundException {
        playerNumber = player;
        vm = new VM(filePath);
        angle = startingAngle;
        XPos = startingX;
        YPos = startingY;
        tiledMap = map;
    }

    //Sets the starting X Position of the Car
    public void setXPos(int xPos)
    {
        XPos = xPos;
    }

    //Sets the starting Y Position of the Car
    public void setYPos(int yPos)
    {
        YPos = yPos;
    }

    //Sets te starting angle of the Car
    public void setAngle(int angleInput)
    {
        angle = angleInput;
    }

    //Sets the sprite of the Car object. MUST BE DONE, otherwise program will not work
    public void setSprite(Texture inputTexture)
    {
        sprite = new Sprite(inputTexture);
    }

    //Occurs every time the core game loop runs
    public void doTick()
    {
        //Get commands from the VM
        vm.doTick(this);

        //Moves the car based on velocity
        move();

        //Update current tile position
        double tempNumberX = (tiledMap.getProperties().get("width",Integer.class) * 32) / 550f;
        currentTileX = (int)((XPos/32) * tempNumberX);
        double tempNumberY = (tiledMap.getProperties().get("height",Integer.class) * 32) / 550f;
        currentTileY = (int)((YPos/32) * tempNumberY);

        System.out.println("X Tile: " + currentTileX + " Y Tile: " + currentTileY);

        //Update the sprite's location and angle to match that of the Car object's
        sprite.setRotation(angle);
        sprite.setX((int)XPos - 24); //Offset x by half the width to center
        sprite.setY((int)YPos - 14); //Offset y by half the height to center

    }

    public void accelerate(double velocity)
    {
        //TODO Possibly implement a more realistic acceleration algorithm

        XVelocity += velocity * cos((angle * 3.14159)/180);
        YVelocity += velocity * sin((angle * 3.14159)/180);

        //System.out.println("XVelocity: " + XVelocity + "  YVelocity: " + YVelocity + " Angle: " + angle + " Cos of Angle: " + cos(angle) + " Sin of Angle: " + sin(angle));

    }

    //Decreases X and Y velocity by 50%
    public void brake()
    {
        XVelocity = XVelocity * .5;
        YVelocity = YVelocity * .5;
    }

    public void move()
    {
        XPos += XVelocity;
        YPos += YVelocity;
        //TODO Tweak code to decrease velocity, maybe decrease quicker if car is in grass?
        XVelocity = XVelocity * .75;
        YVelocity = YVelocity * .75;

        //Prevent car from going offscreen
        if(XPos < 0)
            XPos = 0;
        if(YPos < 0)
            YPos = 0;
        if(XPos > 550)
            XPos = 550;
        if(YPos > 550)
            YPos = 550;
    }


    //Sets the car's angle to be X degrees greater than the current angle
    public void turnCounterClockwise(int degrees)
    {
        angle += degrees;
        if(angle >= 360)
            angle -= 360;
    }

    //Sets the car's angle to be X degrees less than the current angle
    public void turnClockwise(int degrees)
    {
        angle -= degrees;
        if(angle < 0)
            angle += 360;
    }


    //TODO Implement checking the map data file to see what is grass/obstacle and what is not
    public boolean checkLeft(int numPixels)
    {
        double tempNumberX = (tiledMap.getProperties().get("width",Integer.class) * 32) / 550f; //TODO Make sure this is implemented
        double tempNumberY = (tiledMap.getProperties().get("height",Integer.class) * 32) / 550f;
        boolean returnValue = false;
        TiledMapTileLayer backgroundLayer1 = (TiledMapTileLayer)tiledMap.getLayers().get(0);

        //Car facing right
        if((angle >=0 && angle <= 45) || (angle >= 316 && angle <= 360))
        {
            adjustedTileY = (int)(((YPos + 14 + numPixels)/32) * tempNumberY); //Adjusted Y Tile to be checked. Current YPos + 1/2 of sprite width (28) + number of pixels away from car to be checked
            if(currentTileY + 1 >= ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight())
                returnValue = true;
            else if(backgroundLayer1.getCell(currentTileX,currentTileY + 1).getTile().getProperties().get("tileType").equals("grass"))
                returnValue = true;
        }
        //Car facing up (north)
        else if(angle >= 46 && angle <= 135)
        {
            adjustedTileX = (int)(((XPos - 14 - numPixels)/32) * tempNumberX); //Adjusted X Tile to be checked. Current XPOS - 1/2 of sprite width (28) - number of pixels away from the car to be checked
            if(currentTileX - 1 <= 0)
            returnValue = true;
            else if(backgroundLayer1.getCell(adjustedTileX, currentTileY).getTile().getProperties().get("tileType").equals("grass"))
                returnValue = true;
        }
        //Car facing left
        else if(angle >= 136 && angle <= 225)
        {
            adjustedTileY = (int)(((YPos - 14 - numPixels)/32) * tempNumberY); //Adjusted Y Tile to be checked. Current YPos - 1/2 of sprite width (28) - number of pixels away from car to be checked
            if(currentTileY <= 0)
                returnValue = true;
            else if(backgroundLayer1.getCell(currentTileX,adjustedTileY).getTile().getProperties().get("tileType").equals("grass"))
                returnValue = true;
        }
        //Car facing down (south)
        else if(angle >= 226 && angle <= 315)
        {
            adjustedTileX = (int)(((XPos + 14 + numPixels)/32) * tempNumberX); //Adjusted X Tile to be checked. Current XPOS + 1/2 of sprite width (28) + number of pixels away from the car to be checked
            if(currentTileX + 1 >= ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth())
                returnValue = true;
            else if(backgroundLayer1.getCell(adjustedTileX, currentTileY).getTile().getProperties().get("tileType").equals("grass"))
                returnValue = true;
        }
            return returnValue;
    }

    public boolean checkRight(int numPixels)
    {
        double tempNumberX = (tiledMap.getProperties().get("width",Integer.class) * 32) / 550f; //TODO Make sure this is implemented
        double tempNumberY = (tiledMap.getProperties().get("height",Integer.class) * 32) / 550f;
        boolean returnValue = false;
        TiledMapTileLayer backgroundLayer1 = (TiledMapTileLayer)tiledMap.getLayers().get(0);

        //Car facing right
        if((angle >=0 && angle <= 45) || (angle >= 316 && angle <= 360))
        {
            adjustedTileY = (int)(((YPos - 14 - numPixels)/32) * tempNumberY); //Adjusted Y Tile to be checked. Current YPos - 1/2 of sprite width (28) - number of pixels away from car to be checked
            if(currentTileY - 1 <= 0)
                returnValue = true;
            //backgroundLayer1.getCell(0, 0).getTile().getProperties().get("tileType")
            else if(backgroundLayer1.getCell(currentTileX,adjustedTileY).getTile().getProperties().get("tileType").equals("grass"))
                returnValue = true;
        }
        //Car facing up (north)
        else if(angle >= 46 && angle <= 135)
        {
            adjustedTileX = (int)(((XPos + 14 + numPixels)/32) * tempNumberX); //Adjusted X Tile to be checked. Current XPOS + 1/2 of sprite width (28) + number of pixels away from the car to be checked
            if(currentTileX + 1 >= ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth())
                returnValue = true;
            else if(backgroundLayer1.getCell(adjustedTileX, currentTileY).getTile().getProperties().get("tileType").equals("grass"))
                returnValue = true;
        }
        //Car facing left
        else if(angle >= 136 && angle <= 225)
        {
            adjustedTileY = (int)(((YPos + 14 + numPixels)/32) * tempNumberY); //Adjusted Y Tile to be checked. Current YPos + 1/2 of sprite width (28) + number of pixels away from car to be checked
            if(currentTileY + 1 >= ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight())
                returnValue = true;
            else if(backgroundLayer1.getCell(currentTileX,adjustedTileY).getTile().getProperties().get("tileType").equals("grass"))
                returnValue = true;
        }
        //Car facing down (south)
        else if(angle >= 226 && angle <= 315)
        {
            adjustedTileX = (int)(((XPos - 14 - numPixels)/32) * tempNumberX); //Adjusted X Tile to be checked. Current XPOS - 1/2 of sprite width (28) - number of pixels away from the car to be checked
            if(currentTileX - 1 <= 0)
                returnValue = true;
            else if(backgroundLayer1.getCell(adjustedTileX, currentTileY).getTile().getProperties().get("tileType").equals("grass"))
                returnValue = true;
        }

        return returnValue;
    }

    public boolean checkFront(int numPixels)
    {
        boolean returnValue = false;
        TiledMapTileLayer backgroundLayer1 = (TiledMapTileLayer)tiledMap.getLayers().get(0);

        //Car facing right
        if((angle >=0 && angle <= 45) || (angle >= 316 && angle <= 360))
        {
            if(currentTileX + 1 >= ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth())
                returnValue = true;
            else if(backgroundLayer1.getCell(currentTileX +  1, currentTileY).getTile().getProperties().get("tileType").equals("grass"))
                returnValue = true;
        }
        //Car facing up (north)
        else if(angle >= 46 && angle <= 135)
        {
            if(currentTileY + 1 >= ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight())
                returnValue = true;
            else if(backgroundLayer1.getCell(currentTileX, currentTileY + 1).getTile().getProperties().get("tileType").equals("grass"))
                returnValue = true;
        }
        //Car facing left
        else if(angle >= 136 && angle <= 225)
        {
            if(currentTileX - 1 <= 0)
                returnValue = true;
            else if(backgroundLayer1.getCell(currentTileX - 1, currentTileY).getTile().getProperties().get("tileType").equals("grass"))
                returnValue = true;
        }
        //Car facing down (south)
        else if(angle >= 226 && angle <= 315)
        {
            if(currentTileY - 1 <= 0)
                returnValue = true;
            else if(backgroundLayer1.getCell(currentTileX, currentTileY - 1).getTile().getProperties().get("tileType").equals("grass"))
                returnValue = true;
        }

        return returnValue;
    }

    //Returns the X Coordinate of the Car (larger number = farther right. Starts at 0)
    public double getXPos()
    {
        return XPos;
    }

    //Returns the Y Coordinate of the Car (Larger number = farther up. Starts at 0)
    public double getYPos()
    {
        return YPos;
    }

    //Returns the angle of the Car
    public float getAngle(){
        return angle;
    }

    //Returns the Sprite object of the Car
    public Sprite getSprite()
    {
        return sprite;
    }

}
