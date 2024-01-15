// used to store the player's data
// ICS Summative, Bala V, Darian Y, ICS4U 2024. LightSpeed racing game.

import java.awt.*;
import java.awt.event.*;

public class RaceCompetitor extends Rectangle{
    // car dimensions are rounded up to nearest

    public Car raceCar;

    public int acceldir = 0;
    public double carAngle = 0;
    public double forwardSpeed = 0;
    public double AngularVelocity = 0; // degrees per frame. Turning speed is set for each car.
    public double centerX;
    public double centerY;

    public double carTurnSpeed = 0;
    public boolean reverseGearEngaged = false;


    //constructor creates car at given location with given dimensions
    public RaceCompetitor(int x, int y, Car currentCar){
        super(x, y, currentCar.CAR_LENGTH, currentCar.CAR_WIDTH);
        raceCar =  currentCar; // saves car's data
        centerX = x;
        centerY = y;

    }

    //called from GamePanel when any keyboard input is detected
    //updates the position of the car based on user input
    public void keyPressed(KeyEvent e){
        if(e.getKeyChar() == 'd'){// sets turning speed and direction
            carTurnSpeed = raceCar.calculateAngularVelocity(forwardSpeed);
        }

        if(e.getKeyChar() == 'a'){
            carTurnSpeed  = -raceCar.calculateAngularVelocity(forwardSpeed);
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
            carTurnSpeed = 0;
        }

        if(e.getKeyChar() == 'd'){
            carTurnSpeed = 0;
        }
    }


    //updates the current location of the car
    public void move(){
        double xVelocity;
        double yVelocity;

        carAngle += carTurnSpeed * (1.0/60); // turns the car.
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
    }

    //called frequently from the GamePanel class
    //draws the current location of the car to the screen
    public void draw(Graphics2D g){ // draws car at center location on screen.
        g.drawImage(raceCar.carImage,(int) (600 * GamePanel.scaleMultiplier + 0.5 - raceCar.CAR_WIDTH * GamePanel.pixelsPerMeter/2), (int) (900 * GamePanel.scaleMultiplier + 0.5 - raceCar.CAR_WIDTH * GamePanel.pixelsPerMeter/2), null);
    }

}