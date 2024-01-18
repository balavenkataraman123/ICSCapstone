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
    // car dimensions are rounded up to nearest

    public Car raceCar;

    public int acceldir = 0;
    public double carAngle = 0;
    public double forwardSpeed = 0;
    public double centerX;
    public double centerY;

    public double carTurndir = 0;
    public boolean reverseGearEngaged = false;

    public int numCH = 0;
    public double lastCHX = 99999;
    public double lastCHY = 99999;
    public PrintWriter out;
    public ArrayList<String> ghostCoords = new ArrayList<>();





    //constructor creates car at given location with given dimensions
    public RaceCompetitor(double x, double y, double inpCarAngle, int carID, String chosenTrack){
        super((int) x, (int) y, 2,5);
        raceCar = new Car(carID); // saves car's data
        centerX = x;
        centerY = y;
        carAngle = inpCarAngle;
        ghostCoords.add(carID + "\n");
        trackingFilePath = chosenTrack + "_Ghost.txt";
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
        ghostCoords.add(centerX + " " + centerY + " " + carAngle + "\n");
    }
    public void verifyCheckPoint(){
        if(Math.max (Math.abs(centerY - lastCHY), Math.abs(centerX - lastCHX)) >= 20){
            numCH += 1;
            lastCHX = centerX;
            lastCHY = centerY;
        }

    }
    public void bounce(int crashes){
        if (crashes != 0){
            if(reverseGearEngaged){
                forwardSpeed = -forwardSpeed;
            }
        }
        if((crashes & 3) != 0){
            System.out.println("Front");
            centerX -= Math.sin(carAngle) * forwardSpeed / 30;
            centerY += Math.cos(carAngle) * forwardSpeed / 30;
            forwardSpeed = 0;
        }
        if((crashes & 9) != 0){
            System.out.println("Left");
            centerX -= Math.cos(carAngle) * forwardSpeed / 30;
            centerY += Math.sin(carAngle) * forwardSpeed /30;
            forwardSpeed = 0;
        }
        if((crashes & 12) != 0){
            System.out.println("back");
            centerX += Math.sin(carAngle) * forwardSpeed / 30;
            centerY -= Math.cos(carAngle) * forwardSpeed / 30;
            forwardSpeed = 0;
        }
        if((crashes & 6) != 0){
            System.out.println("Right");
            centerX += Math.cos(carAngle) * forwardSpeed / 30;
            centerY -= Math.sin(carAngle) * forwardSpeed / 30;
            forwardSpeed = 0;
        }
    }

    //called frequently from the GamePanel class
    //draws the current location of the car to the screen
    public void draw(Graphics2D g){ // draws car at center location on screen.
        g.drawImage(raceCar.carImage,(int) (600 * GamePanel.scaleMultiplier + 0.5 - raceCar.CAR_WIDTH * GamePanel.pixelsPerMeter/2), (int) (900 * GamePanel.scaleMultiplier + 0.5 - raceCar.CAR_WIDTH * GamePanel.pixelsPerMeter/2), null);
    }
    public void writeCoords(){
        File file = new File(trackingFilePath);
        if(FILE_WRITING){
            try{
                file.delete();
                out = new PrintWriter(trackingFilePath);
                for(String e : ghostCoords){
                    out.write(e);
                }
                out.close();


            }
            catch(Exception e){}
        }

    }

}