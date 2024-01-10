/* PlayerBall class defines behaviours for the player-controlled ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
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





    //constructor creates ball at given location with given dimensions
    public RaceCompetitor(int x, int y, Car currentCar){
        super(x, y, currentCar.CAR_LENGTH, currentCar.CAR_WIDTH);
        raceCar =  currentCar;
        centerX = x;
        centerY = y;

    }

    //called from GamePanel when any keyboard input is detected
    //updates the direction of the ball based on user input
    //if the keyboard input isn't any of the options (d, a, w, s), then nothing happens
    public void keyPressed(KeyEvent e){
        if(e.getKeyChar() == 'd'){
            carTurnSpeed = raceCar.calculateAngularVelocity(forwardSpeed);
        }

        if(e.getKeyChar() == 'a'){
            carTurnSpeed  = -raceCar.calculateAngularVelocity(forwardSpeed);
        }

        if(e.getKeyChar() == 'w'){
            System.out.print("vroomvroom");

            acceldir = 1;
        }

        if(e.getKeyChar() == 's'){
            acceldir = -1;

        }
    }

    //called from GamePanel when any key is released (no longer being pressed down)
    //Makes the car stop moving in that direction
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
        //System.out.println(acceleration);
        carAngle += carTurnSpeed * (1.0/60);
        forwardSpeed = Math.max(0, Math.min(raceCar.maxSpeed, forwardSpeed + acceldir *raceCar.calculateAcceleration(acceldir, forwardSpeed)));
        //System.out.println("Speed: " + forwardSpeed);
        if(reverseGearEngaged){
            forwardSpeed = -forwardSpeed;
        }
        double xVelocity = forwardSpeed * Math.sin(carAngle);
        double yVelocity = -forwardSpeed * Math.cos(carAngle);
        centerX += xVelocity * (1.0/60);
        centerY += yVelocity * (1.0/60);
    }

    //called frequently from the GamePanel class
    //draws the current location of the ball to the screen
    public void draw(Graphics2D g){
        g.drawImage(raceCar.carImage,(int) (600 + 0.5 - raceCar.CAR_WIDTH * GamePanel.pixelsPerMeter/2), (int) (900 + 0.5 - raceCar.CAR_WIDTH * GamePanel.pixelsPerMeter/2), null);
        //g.fillRect(, raceCar.CAR_LENGTH*GamePanel.pixelsPerMeter);
    }

}