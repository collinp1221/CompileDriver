/*package com.mygdx.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;

public class VM {

    private String filePath; //The path for the file to be read
    private String[] assemblyArray = new String[1000]; //Can store up to 1000 lines of code for now //TODO Possibly dynamically adjust size as needed
    private int totalLines = 0; //Total number of lines in the file
    private int currentLine; //Currently selected line of the file
    private Hashtable labelTable = new Hashtable<String, Integer>(); //Hashtable to store all LBLs
    private int recursionCount = 0; //Used to track how many times recursion has occurred, to prevent an infinite loop


    //TODO If file has more than 1000 lines of code, display some sort of error
    public VM(String file) throws FileNotFoundException {
        filePath = file;
        File fileObject = new File(file);
        Scanner scanner = new Scanner(fileObject);

        //Copy entirety of file to array assemblyArray, to make it easier to work with
        while(scanner.hasNextLine())
        {
            assemblyArray[totalLines] = scanner.nextLine(); //Starts at 0
            totalLines ++; //Total number of lines
        }


        //Add all LBL commands to the hashtable
        for(int i=0;i<totalLines;i++)
        {
            System.out.println("Going through hashTable loop, i=" + i);
            //TODO If there are any empty lines in file, this probably breaks. Fix/check that
            String subString = assemblyArray[i].substring(0,3); //In a normal scenario, this would be LBL
            String subString2 = assemblyArray[i].substring(4); //Get a substring from position 4 (after LBL and the space). Represents the LBL name

            System.out.println("subString = " + subString);
            System.out.println("subString2 = " + subString2);

            //TODO Maybe check to see if there is a space as the last character, and remove it
            if(subString.equalsIgnoreCase("LBL"))
            {
                labelTable.put(subString2,i); //Key, Value. Key is the Label name in string form, value is line number
            }
        }
    }

    //Occurs every gametick. Reads the next command, and executes a method in car Car accordingly
    public void doTick(Car car)
    {
        System.out.println("Starting VM.doTick()");


        //LBL Command
        //Sets a label to the line that it is on, which can later be jumped to. Then executes the next line of assembly code on the same tick
        //LBL Test
        if(assemblyArray[currentLine].length() >= 2) {
            if (assemblyArray[currentLine].substring(0, 2).equalsIgnoreCase("LBL") && recursionCount < 3) {

                recursionCount ++;
                //Increment the current line
                currentLine ++;
                if(currentLine >= totalLines)
                    currentLine = 0;

                //TODO If anything were to break or be buggy, it would probably be this. Have someone try to break the program by making an assembly program with only LBLs
                //Increment the recursion counter by one. If the method calls itself more than 3 times, it will stop recursing (to prevent an infinite loop)
                recursionCount ++;
                //Do another tick. This prevents LBLs from causing the car to effectively do nothing for a gametick
                doTick(car);
            }
            else if(recursionCount >= 3)
            {
                recursionCount = 0;
            }
        }


        //JMP Command //TODO Fix this? (might work)
        //Jumps to a line, based on the LBL (label)
        //JMP Test
        if(assemblyArray[currentLine].length() >= 3)
        {
            if(assemblyArray[currentLine].substring(0,3).equalsIgnoreCase("JMP"))
            {
                String subString = assemblyArray[currentLine].substring(4); //Get a substring of everything after JMP_
                if(labelTable.containsKey(subString))
                {
                    currentLine = (Integer) labelTable.get(subString); //No idea why this doesnt work..
                }
                else
                {
                    //TODO Implement some sort of ingame error message for this
                    System.out.println("JMP " + subString + "in line " + (currentLine + 1) + " Command has failed! Label cannot be found!");
                }
            }
        }





        //ACCELERATE Command
        //Accelerates the car in the chosen direction by a chosen speed, between 0 and 4.9 //TODO Decide whether to increase or decrease this amount. Probably decrease, 1.9 feels a bit high
        //Number may be a single digit integer, or a decimal with one digit before and after the decimal point
        //Example: ACCELERATE 1.7
        //TODO If the file is empty, this (maybe) breaks and the program crashes. Fix this
        if(assemblyArray[currentLine].length() >= 10) { //Length of the string is checked, to prevent an attempt to get a substring that is larger than the actual string
            if (assemblyArray[currentLine].substring(0, 10).equalsIgnoreCase("ACCELERATE")) //Checks if the first 10 characters are ACCELERATE
            {
                System.out.println("Starting ACCELERATE Submethod");
                String subString = assemblyArray[currentLine].substring(11); //Get a substring from after the space after ACCELERATE (should just contain the number)
                System.out.println("subString is " + subString + "\nSubstring length is " + subString.length() + " charAt(0) is " + subString.charAt(0));
                double accelerateAmount = 0; //How much speed to accelerate at. Will be modified to equal the value entered by the user
                //X.Y
                if (subString.length() == 3 && subString.charAt(1) == '.') {
                    //Get the first character (# before decimal) and add it to the total
                    if (subString.charAt(0) == '0')
                        accelerateAmount += 0; //Redundant, but I'm keeping it here so that the program does not recognize 0.X as an error
                    else if (subString.charAt(0) == '1')
                        accelerateAmount += 1;
                    else if (subString.charAt(0) == '2')
                        accelerateAmount += 1;
                    else if (subString.charAt(0) == '3')
                        accelerateAmount += 1;
                    else if (subString.charAt(0) == '4')
                        accelerateAmount += 1;
                    else {
                        //TODO add in some form of error message on screen. Maybe dev cycle 1?
                        System.out.println("ERROR: Character before the decimal point of ACCELERATE in VM.java is invalid");
                    }

                    //Get the character in position 2 of the substring (number after decimal), and add it to the
                    if (subString.charAt(2) == '0')
                        accelerateAmount += .0; //Redundant, but I'm keeping it here so that the program does not recognize X.0 as an error
                    else if (subString.charAt(2) == '1')
                        accelerateAmount += .1;
                    else if (subString.charAt(2) == '2')
                        accelerateAmount += .2;
                    else if (subString.charAt(2) == '3')
                        accelerateAmount += .3;
                    else if (subString.charAt(2) == '4')
                        accelerateAmount += .4;
                    else if (subString.charAt(2) == '5')
                        accelerateAmount += .5;
                    else if (subString.charAt(2) == '6')
                        accelerateAmount += .6;
                    else if (subString.charAt(2) == '7')
                        accelerateAmount += .7;
                    else if (subString.charAt(2) == '8')
                        accelerateAmount += .8;
                    else if (subString.charAt(2) == '9')
                        accelerateAmount += .9;
                    else {
                        //TODO add in some form of error message on screen. Maybe dev cycle 1?
                        System.out.println("ERROR: Character after the decimal point of ACCELERATE in VM.java is invalid");
                    }
                }
                //.X
                else if (subString.length() == 2 && subString.charAt(0) == '.') {
                    //Get the character in position 2 of the substring (number after decimal), and add it to the
                    if (subString.charAt(1) == '0')
                        accelerateAmount += .0; //Redundant, but I'm keeping it here so that the program does not recognize X.0 as an error
                    else if (subString.charAt(1) == '1')
                        accelerateAmount += .1;
                    else if (subString.charAt(1) == '2')
                        accelerateAmount += .2;
                    else if (subString.charAt(1) == '3')
                        accelerateAmount += .3;
                    else if (subString.charAt(1) == '4')
                        accelerateAmount += .4;
                    else if (subString.charAt(1) == '5')
                        accelerateAmount += .5;
                    else if (subString.charAt(1) == '6')
                        accelerateAmount += .6;
                    else if (subString.charAt(1) == '7')
                        accelerateAmount += .7;
                    else if (subString.charAt(1) == '8')
                        accelerateAmount += .8;
                    else if (subString.charAt(1) == '9')
                        accelerateAmount += .9;
                    else {
                        //TODO add in some form of error message on screen. Maybe dev cycle 1?
                        System.out.println("ERROR: Character after the decimal point of ACCELERATE in method doTick of VM.java is invalid");
                    }
                }
                //X
                else if (subString.length() == 1) {
                    //Get the first character (integer value) and add it to the total
                    if (subString.charAt(0) == '0')
                        accelerateAmount += 0; //Redundant, but I'm keeping it here so that the program does not recognize 0.X as an error
                    else if (subString.charAt(0) == '1')
                        accelerateAmount += 1;
                    else if (subString.charAt(0) == '2')
                        accelerateAmount += 1;
                    else if (subString.charAt(0) == '3')
                        accelerateAmount += 1;
                    else if (subString.charAt(0) == '4')
                        accelerateAmount += 1;
                    else {
                        //TODO add in some form of error message on screen. Maybe dev cycle 1?
                        System.out.println("ERROR: Character before the decimal point of ACCELERATE in method doTick of VM.java is invalid");
                    }
                }
                //If invalid input, accelerate with a factor of 0
                else {
                    //TODO Add in some sort of error message on-screen here
                    System.out.println("ERROR: Invalid ACCELERATE argument in method doTick() of VM.java. ");
                }

                System.out.println("accelerateAmount is " + accelerateAmount);
                //Accelerate the car based on the input
                car.accelerate(accelerateAmount);
            }
            else
            {
                //TODO Provide some sort of ingame error message
                //System.out.println("ERROR: Invalid command in line " + (currentLine + 1) + " of the assembly file");
            }
        }

        //TURNCOUNTERCLOCKWISE Command
        //Rotates the car counterclockwise between 1 and 9 degrees. Can only process integers (can't rotate 1.4 degrees)
        //TURNCOUNTERCLOCKWISE 6
        if(assemblyArray[currentLine].length() >= 20)
        {
            if(assemblyArray[currentLine].substring(0, 20).equalsIgnoreCase("TURNCOUNTERCLOCKWISE"))
            {
                System.out.println("In TURNCOUNTERCLOCKWISE Method");
                String subString = assemblyArray[currentLine].substring(21);
                int turnAmount = 0;

                //X
                if(subString.length() == 1)
                {
                    if(subString.charAt(0) == '0')
                        turnAmount += 0; //Redundant, but prevents detection of TURNCOUNTERCLOCKWISE 0 as an error
                    else if(subString.charAt(0) == '1')
                        turnAmount += 1;
                    else if(subString.charAt(0) == '2')
                        turnAmount += 2;
                    else if(subString.charAt(0) == '3')
                        turnAmount += 3;
                    else if(subString.charAt(0) == '4')
                        turnAmount += 4;
                    else if(subString.charAt(0) == '5')
                        turnAmount += 5;
                    else if(subString.charAt(0) == '6')
                        turnAmount += 6;
                    else if(subString.charAt(0) == '7')
                        turnAmount += 7;
                    else if(subString.charAt(0) == '8')
                        turnAmount += 8;
                    else if(subString.charAt(0) == '9')
                        turnAmount += 9;
                }
                else
                {
                    //TODO Add in some sort of ingame error message.
                    System.out.println("ERROR: Invalid Argument for TURNCOUNTERCLOCKWISE in method doTick() of VM.java");
                }

                //Rotate the car counterclockwise the amount specified by the user
                car.turnCounterClockwise(turnAmount);
            }
        }

        //TURNCLOCKWISE Command
        //Rotates the car counterclockwise between 1 and 9 degrees. Can only process integers (can't rotate 1.4 degrees)
        //TURNCLOCKWISE 6
        if(assemblyArray[currentLine].length() >= 13)
        {
            if(assemblyArray[currentLine].substring(0, 13).equalsIgnoreCase("TURNCLOCKWISE"))
            {
                System.out.println("In TURNCLOCKWISE Method");
                String subString = assemblyArray[currentLine].substring(14);
                int turnAmount = 0;

                //X
                if(subString.length() == 1)
                {
                    if(subString.charAt(0) == '0')
                        turnAmount += 0; //Redundant, but prevents detection of TURNCLOCKWISE 0 as an error
                    else if(subString.charAt(0) == '1')
                        turnAmount += 1;
                    else if(subString.charAt(0) == '2')
                        turnAmount += 2;
                    else if(subString.charAt(0) == '3')
                        turnAmount += 3;
                    else if(subString.charAt(0) == '4')
                        turnAmount += 4;
                    else if(subString.charAt(0) == '5')
                        turnAmount += 5;
                    else if(subString.charAt(0) == '6')
                        turnAmount += 6;
                    else if(subString.charAt(0) == '7')
                        turnAmount += 7;
                    else if(subString.charAt(0) == '8')
                        turnAmount += 8;
                    else if(subString.charAt(0) == '9')
                        turnAmount += 9;
                }
                else
                {
                    //TODO Add in some sort of ingame error message.
                    //System.out.println("ERROR: Invalid Argument for TURNCLOCKWISE in method doTick() of VM.java");
                }

                //Rotate the car counterclockwise the amount specified by the user
                car.turnClockwise(turnAmount);
            }
        }

        //BRAKE Command
        if(assemblyArray[currentLine].length() >= 5)
        {
            if(assemblyArray[currentLine].substring(0, 5).equalsIgnoreCase("BRAKE"))
            {
                car.brake();
            }
        }

        //TODO Make a String to int java file to do all of the conversion there (code is too messy as is)
        //CHKLEFT Command
        //Checks to see if there is anything to the left of the car within a specified distance. If not, skips the next line of the program. Can take an int between 0 and 999 (inclusive)
        //CHKLEFT 25
        if(assemblyArray[currentLine].length() >= 7)
        {
            if(assemblyArray[currentLine].substring(0,7).equalsIgnoreCase("CHKLEFT") )
            {
                String subString = assemblyArray[currentLine].substring(7);
                int numPixels = stringToInt(subString); //The number of pixels to check to the left of the car

                //If checkLeft returns false, skip the next line of the assembly file
                if(!car.checkLeft(numPixels))
                {
                    currentLine ++;
                    if(currentLine >= totalLines)
                        currentLine = 0;
                }

            }
        }




        //CHKRIGHT Command
        //Checks to see if there is anything to the right of the car within a specified distance. If not, skips the next line of the program. Can take an int between 0 and 999 (inclusive)
        //CHKRIGHT 25
        if(assemblyArray[currentLine].length() >= 8)
        {
            if(assemblyArray[currentLine].substring(0,8).equalsIgnoreCase("CHKRIGHT") )
            {
                String subString = assemblyArray[currentLine].substring(8);
                int numPixels = stringToInt(subString); //The number of pixels to check to the right of the car

                //If checkLeft returns false, skip the next line of the assembly file
                if(!car.checkRight(numPixels))
                {
                    currentLine ++;
                    if(currentLine >= totalLines)
                        currentLine = 0;
                }
            }
        }




        //CHKFRONT Command
        //Checks to see if there is anything in front of the car within a specified distance. If not, skips the next line of the program. Can take an int between 0 and 999 (inclusive)
        //CHKFRONT 25
        if(assemblyArray[currentLine].length() >= 8)
        {
            if(assemblyArray[currentLine].substring(0,8).equalsIgnoreCase("CHKFRONT") )
            {
                String subString = assemblyArray[currentLine].substring(8);
                int numPixels = stringToInt(subString); //The number of pixels to check to the front of the car

                //If checkFront returns false, skip the next line of the assembly file
                if(!car.checkFront(numPixels))
                {
                    currentLine ++;
                    if(currentLine >= totalLines)
                        currentLine = 0;
                }
            }
        }


        //TODO If a line is blank, ignore it

        //Increment current line. If the current line is outside the bounds of the file, set it to the first line
        currentLine ++;
        if(currentLine >=  totalLines)
            currentLine = 0;
    }

    private int stringToInt(String input)
    {
        int output = 0;
        boolean invalidChar = false;


        if(input.length() == 3)
        {
            if(input.charAt(0) == '0')
                output += 000; //Redundant, but prevents errors from occurring for numbers like 073
            else if(input.charAt(0) == '1')
                output += 100;
            else if(input.charAt(0) == '2')
                output += 200;
            else if(input.charAt(0) == '3')
                output += 300;
            else if(input.charAt(0) == '4')
                output += 400;
            else if(input.charAt(0) == '5')
                output += 500;
            else if(input.charAt(0) == '6')
                output += 600;
            else if(input.charAt(0) == '7')
                output += 700;
            else if(input.charAt(0) == '8')
                output += 800;
            else if(input.charAt(0) == '9')
                output += 900;
            else
            {
                invalidChar = true;
            }

            if(input.charAt(1) == '0')
                output += 00; //Redundant, but prevents errors from occurring for numbers like 073
            else if(input.charAt(1) == '1')
                output += 10;
            else if(input.charAt(1) == '2')
                output += 20;
            else if(input.charAt(1) == '3')
                output += 30;
            else if(input.charAt(1) == '4')
                output += 40;
            else if(input.charAt(1) == '5')
                output += 50;
            else if(input.charAt(1) == '6')
                output += 60;
            else if(input.charAt(1) == '7')
                output += 70;
            else if(input.charAt(1) == '8')
                output += 80;
            else if(input.charAt(1) == '9')
                output += 90;
            else
            {
                invalidChar = true;
            }

            if(input.charAt(2) == '0')
                output += 0; //Redundant, but prevents errors from occurring for numbers like 073
            else if(input.charAt(2) == '1')
                output += 1;
            else if(input.charAt(2) == '2')
                output += 2;
            else if(input.charAt(2) == '3')
                output += 3;
            else if(input.charAt(2) == '4')
                output += 4;
            else if(input.charAt(2) == '5')
                output += 5;
            else if(input.charAt(2) == '6')
                output += 6;
            else if(input.charAt(2) == '7')
                output += 7;
            else if(input.charAt(2) == '8')
                output += 8;
            else if(input.charAt(2) == '9')
                output += 9;
            else
            {
                invalidChar = true;
            }
        }
        else if(input.length() == 2)
        {
            if(input.charAt(0) == '0')
                output += 00; //Redundant, but prevents errors from occurring for numbers like 073
            else if(input.charAt(0) == '1')
                output += 10;
            else if(input.charAt(0) == '2')
                output += 20;
            else if(input.charAt(0) == '3')
                output += 30;
            else if(input.charAt(0) == '4')
                output += 40;
            else if(input.charAt(0) == '5')
                output += 50;
            else if(input.charAt(0) == '6')
                output += 60;
            else if(input.charAt(0) == '7')
                output += 70;
            else if(input.charAt(0) == '8')
                output += 80;
            else if(input.charAt(0) == '9')
                output += 90;
            else
            {
                invalidChar = true;
            }

            if(input.charAt(1) == '0')
                output += 0; //Redundant, but prevents errors from occurring for numbers like 073
            else if(input.charAt(1) == '1')
                output += 1;
            else if(input.charAt(1) == '2')
                output += 2;
            else if(input.charAt(1) == '3')
                output += 3;
            else if(input.charAt(1) == '4')
                output += 4;
            else if(input.charAt(1) == '5')
                output += 5;
            else if(input.charAt(1) == '6')
                output += 6;
            else if(input.charAt(1) == '7')
                output += 7;
            else if(input.charAt(1) == '8')
                output += 8;
            else if(input.charAt(1) == '9')
                output += 9;
            else
            {
                invalidChar = true;
            }
        }
        else if(input.length() == 1)
        {
            if(input.charAt(0) == '0')
                output += 0; //Redundant, but prevents errors from occurring for numbers like 073
            else if(input.charAt(0) == '1')
                output += 1;
            else if(input.charAt(0) == '2')
                output += 2;
            else if(input.charAt(0) == '3')
                output += 3;
            else if(input.charAt(0) == '4')
                output += 4;
            else if(input.charAt(0) == '5')
                output += 5;
            else if(input.charAt(0) == '6')
                output += 6;
            else if(input.charAt(0) == '7')
                output += 7;
            else if(input.charAt(0) == '8')
                output += 8;
            else if(input.charAt(0) == '9')
                output += 9;
            else
            {
                invalidChar = true;
            }
        }
        else
        {
            invalidChar = true; //Invalid length
        }

        if(invalidChar)
            return 0;
        else
            return output;
    }

}
*/