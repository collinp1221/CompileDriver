package com.mygdx.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import javax.swing.*;
import java.awt.*;
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
    private float maxX = 999999;

    //Maximum Y value that the Car can travel to.
    private float maxY = 999999;

    //TODO Implement this. Probably do this in 1st design cycle?
    //Tracks the most recent flag passed. Starts at 0 (Start/Finish Line)
    private int currentFlag = 0;

    //Tracks what player the car is
    private int playerNumber;

    //TODO Test this
    private Texture texture;

    //Sprite of the car
    private Sprite sprite;

    //VM Class, used to read and interpret the user's assembly file
    private VM vm;

    //TODO Implement
    private Robot robot;



    //Constructor Classes

    //Default constructor with only player number, uses the default AI
    public Car(int player) throws FileNotFoundException {
        playerNumber = player;

        String workingDirectory = System.getProperty("user.dir");
        String absoluteFilePath = workingDirectory + File.separator + "ai" + File.separator + "defaultAI.txt";

        vm = new VM(absoluteFilePath);
    }

    //Constructor class with custom AI
    public Car(int player, String filePath, int startingAngle, int startingX, int startingY, int maxXPos, int maxYPos) throws FileNotFoundException {
        playerNumber = player;
        vm = new VM(filePath); //TODO Make this actually work (it doesn't yet)
        angle = startingAngle;
        XPos = startingX;
        YPos = startingY;
        maxX = maxXPos;
        maxY = maxYPos;
    }

    //TODO Consider removing setStartingX/Y in favor of declaring these on creation of the object

    //Sets the starting X Position of the Car
    public void setStartingX(int startingX)
    {
        XPos = startingX;
    }

    //Sets the starting Y Position of the Car
    public void setStartingY(int startingY)
    {
        YPos = startingY;
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

    public void setMapTexture(Texture mapTexture)
    {
        texture = mapTexture;
    }


    //Occurs every time the core game loop runs
    public void doTick()
    {
        //Get commands from the VM
        vm.doTick(this);

        //Moves the car based on velocity
        move();

        //Update the sprite's location and angle to match that of the Car object's
        sprite.setRotation(angle);
        sprite.setX((int)XPos - 24); //Offset x by half the width to center
        sprite.setY((int)YPos - 14); //Offset y by half the height to center

    }

    //TODO: Finish and comment this
    public void accelerate(double velocity)
    {
        //TODO Add proper acceleration

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
        System.out.println("XVelocity is: " + XVelocity + " YVelocity is " + YVelocity);
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
        System.out.println("In method turnCounterClockwise of Car.java. Angle is " + angle);
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


    //TODO Implement checking the map data file to get the road color
    public boolean checkLeft(int numPixels)
    {
        if(numPixels <= 0)
            return false;
        else
        {

            Color borderColor = new Color(255,255,255);
            texture.getTextureData().prepare(); //Required to get texture data
            Pixmap pixmap = texture.getTextureData().consumePixmap(); //Get texture data and save it as a pixmap

            //Car facing right
            if((angle >=0 && angle <= 45) || (angle >= 316 && angle <= 360))
            {

            }
            //Car facing up (north)
            else if(angle >= 46 && angle <= 135)
            {

            }
            //Car facing left
            else if(angle >= 136 && angle <= 225)
            {

            }
            //Car facing down (south)
            else if(angle >= 226 && angle <= 315)
            {

            }

/*
            for(int i=0;i<numPixels;i++) //X
            {
                for(int j=0;j<48;j++) //Y
                {
                    Color originalColor = new Color(pixmap.getPixel((int)XPos,(int)YPos)); //Get the color of selected pixel
                    Color convertedColor = new Color(originalColor.getBlue(),originalColor.getGreen(),originalColor.getRed()); //Convert to RGB, since LibGDX exports color as BGR for some reason...

                }
            }
*/

            return false;
        }
    }

    public boolean checkRight(int numPixels)
    {

        //Car facing right
        if((angle >=0 && angle <= 45) || (angle >= 316 && angle <= 360))
        {

        }
        //Car facing up (north)
        else if(angle >= 46 && angle <= 135)
        {

        }
        //Car facing left
        else if(angle >= 136 && angle <= 225)
        {

        }
        //Car facing down (south)
        else if(angle >= 226 && angle <= 315)
        {

        }

        return false;
    }

    public boolean checkFront(int numPixels)
    {

        //Car facing right
        if((angle >=0 && angle <= 45) || (angle >= 316 && angle <= 360))
        {

        }
        //Car facing up (north)
        else if(angle >= 46 && angle <= 135)
        {

        }
        //Car facing left
        else if(angle >= 136 && angle <= 225)
        {

        }
        //Car facing down (south)
        else if(angle >= 226 && angle <= 315)
        {

        }

        return false;
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
