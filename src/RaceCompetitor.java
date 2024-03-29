// used to store the player's data
// ICS Summative, Bala V, Darian Y, ICS4U 2024. LightSpeed racing game.

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class RaceCompetitor extends Rectangle{
    public final boolean FILE_WRITING = true;
    public String trackingFilePath = "";
    // variables for the car physics

    public Car raceCar; // car's config files.
    public int acceldir = 0; // direction of acceleration
    public double forwardSpeed = 0; // car's speed

    public double carAngle, centerX, centerY; // location of car
    public double carTurndir = 0; // turn direction
    public boolean reverseGearEngaged = false; // stores whether the user is going forward or backwards.
    public double health; // car's health
    // the above variable might need some explaining to Americans.
    public int numCH = 0; // number of checkpoints
    public double lastCHX = 99999; // checkpoint location
    public double lastCHY = 99999;
    public PrintWriter out; // writes the files to the ghost.
    public ArrayList<String> ghostCoords = new ArrayList<>(); // location log, as you become the new ghost if you beat it.




    //constructor creates car at given location with given car data.
    public RaceCompetitor(double x, double y, double inpCarAngle, int carID, String chosenTrack){
        super((int) x, (int) y, 2,5);
        raceCar = new Car(carID); // saves car's data
        centerX = x;
        centerY = y;
        carAngle = inpCarAngle;
        ghostCoords.add(carID + "\n"); // logs the car you have.
        health = raceCar.health;
        trackingFilePath = chosenTrack + "_Ghost.txt"; // sets up "ghost" file. You become the new ghost if you beat it.
    }

    //called from GamePanel when any keyboard input is detected
    //updates the position of the car based on user input
    public void keyPressed(KeyEvent e){
        if(e.getKeyChar() == 'd'){// sets turning speed and direction
            carTurndir = 1;
        }
        if(e.getKeyChar() == 'a'){
            carTurndir = -1;
        }
        if(e.getKeyChar() == 'w'){ // sets direction of acceleration when w or s is pressed
            acceldir = 1;
        }
        if(e.getKeyChar() == 's'){
            acceldir = -1;
            if(Math.abs(carTurndir) == 1) { // simulates oversteer on deceleration, when a car's weight shifts to the front and gives front wheels more grip to turn.
                carTurndir *= 1.25;
            }
        }
        if(e.getKeyChar() == 'r'){ // switches direction of the car, but only if the car is stopped.
            if(forwardSpeed == 0){
                reverseGearEngaged = !reverseGearEngaged;
            }
        }
    }

    //called from GamePanel when any key is released (no longer being pressed down)
    //Makes the car stop turning or accelerating
    public void keyReleased(KeyEvent e){
        if(e.getKeyChar() == 'w'){
            acceldir = 0;
        }

        if(e.getKeyChar() == 's'){
            acceldir = 0;
        }
        if(e.getKeyChar() == 'a'){
            carTurndir = 0;
        }

        if(e.getKeyChar() == 'd'){
            carTurndir = 0;
        }
    }


    //updates the current location of the car
    public void move(){
        double xVelocity;
        double yVelocity;

        carAngle += carTurndir * raceCar.calculateAngularVelocity(forwardSpeed) * (1.0/60); // turns the car.
        forwardSpeed = Math.max(0, Math.min(raceCar.maxSpeed, forwardSpeed + acceldir *raceCar.calculateAcceleration(acceldir, forwardSpeed))); // changes velocity by acceleration
        // Sets the velocity components based on speed and direction
        if(reverseGearEngaged){ // sets velocity in forward or reverse depending on reverse gear.
           xVelocity = -forwardSpeed * Math.sin(carAngle);
           yVelocity = forwardSpeed * Math.cos(carAngle);
        }
        else {
            xVelocity = forwardSpeed * Math.sin(carAngle);
            yVelocity = -forwardSpeed * Math.cos(carAngle);
        }
        centerX += xVelocity * (1.0/60); // updates positioning of the car.
        centerY += yVelocity * (1.0/60);
        ghostCoords.add(centerX + " " + centerY + " " + carAngle + "\n"); // location logging
    }
    public void verifyCheckPoint(){ // checks if the car is at a checkpoint
        if(Math.max (Math.abs(centerY - lastCHY), Math.abs(centerX - lastCHX)) >= 20){
            numCH += 1;
            lastCHX = centerX;
            lastCHY = centerY;
        }

    }
    public void bounce(int crashes){ // bounces the car off the wall if it crashes. Reduces speed and calculates damage inflicted on the car.

        // checks the directions the car has been hit from.
        if(!reverseGearEngaged){ // when driving forward
            if((crashes & 3) == 3){ // full frontal crash
                centerX -= Math.sin(carAngle) * forwardSpeed / 30;
                centerY += Math.cos(carAngle) * forwardSpeed / 30;
                forwardSpeed = 0;
                health -= forwardSpeed;
            }
            else if((crashes & 1) != 0){ // scrape right side
                carAngle += 0.2;
                centerX -= Math.sin(carAngle) * forwardSpeed / 30;
                centerY += Math.cos(carAngle) * forwardSpeed / 30;
                health -= forwardSpeed * 0.5;
                forwardSpeed = Math.pow(forwardSpeed, 0.7);
            }
            else if((crashes & 2) != 0){ // scrape left side
                carAngle -= 0.2;
                centerX -= Math.sin(carAngle) * forwardSpeed / 30;
                centerY += Math.cos(carAngle) * forwardSpeed / 30;
                health -= forwardSpeed * 0.5;
                forwardSpeed = Math.pow(forwardSpeed, 0.7);
            }
        }
        else{ // when driving backwards
            if((crashes & 12) == 12){ // hit the full rear
                centerX += Math.sin(carAngle) * forwardSpeed / 30;
                centerY -= Math.cos(carAngle) * forwardSpeed / 30;
                forwardSpeed = 0;
                health -= forwardSpeed;

            }
            else if((crashes & 4) != 0){ // scrape the right side
                carAngle += 0.2;
                centerX += Math.sin(carAngle) * forwardSpeed / 30;
                centerY -= Math.cos(carAngle) * forwardSpeed / 30;
                health -= forwardSpeed * 0.5;
                forwardSpeed = Math.pow(forwardSpeed, 0.7);
            }
            else if((crashes & 8) != 0){ // scrape the left side
                carAngle -= 0.2;
                centerX += Math.sin(carAngle) * forwardSpeed / 30;
                centerY -= Math.cos(carAngle) * forwardSpeed / 30;
                health -= forwardSpeed * 0.5;
                forwardSpeed = Math.pow(forwardSpeed, 0.7);
            }
        }
    }

    //called frequently from the GamePanel class
    //draws the car in the center of the screen.
    public void draw(Graphics2D g){ // draws car at center location on screean.
        g.drawImage(raceCar.carImage,(int) (600 * GamePanel.scaleMultiplier + 0.5 - raceCar.CAR_WIDTH * GamePanel.pixelsPerMeter/2), (int) (900 * GamePanel.scaleMultiplier + 0.5 - raceCar.CAR_WIDTH * GamePanel.pixelsPerMeter/2), null);
    }
    public void writeCoords(){ // if you do better than the ghost, you become the ghost.
        File file = new File(trackingFilePath); // this function writes your position and direction throughout the race to a file.
        if(FILE_WRITING){
            try{
                file.delete(); // clears old file, and writes it in separate lines.
                out = new PrintWriter(trackingFilePath);
                for(String e : ghostCoords){
                    out.write(e);
                }
                out.close(); // stops memory leak


            }
            catch(Exception e){}
        }

    }

}