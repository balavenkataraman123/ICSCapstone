/* PlayerBall class defines behaviours for the player-controlled ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
import java.awt.*;
import java.awt.event.*;

public class RaceCompetitor extends Rectangle{
    // car dimensions are rounded up to nearest

    public Car raceCar;

    public double acceleration = 0;
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
            carTurnSpeed = raceCar.maxTurnSpeed;
        }

        if(e.getKeyChar() == 'a'){
            carTurnSpeed  = -raceCar.maxTurnSpeed;
        }

        if(e.getKeyChar() == 'w'){
            System.out.print("vroomvroom");
            acceleration = raceCar.calculateAcceleration(forwardSpeed);
        }

        if(e.getKeyChar() == 's'){
            acceleration = - raceCar.calculateAcceleration(forwardSpeed);

        }
    }

    //called from GamePanel when any key is released (no longer being pressed down)
    //Makes the car stop moving in that direction
    public void keyReleased(KeyEvent e){
        if(e.getKeyChar() == 'w'){
            acceleration = 0;
        }

        if(e.getKeyChar() == 's'){
            acceleration = 0;
        }
        if(e.getKeyChar() == 'a'){
            carTurnSpeed = 0;
        }

        if(e.getKeyChar() == 's'){
            carTurnSpeed = 0;
        }
    }


    //updates the current location of the car
    public void move(){
        carAngle += carTurnSpeed;
        forwardSpeed = Math.max(0, Math.min(raceCar.maxSpeed, forwardSpeed + acceleration));
        System.out.println("Moving: Forward speed" + forwardSpeed);
        if(reverseGearEngaged){
            forwardSpeed = -forwardSpeed;
        }
        double xVelocity = forwardSpeed * Math.cos(carAngle);
        double yVelocity = forwardSpeed * Math.sin(carAngle);
        centerX += xVelocity;
        centerY += yVelocity;
    }

    //called frequently from the GamePanel class
    //draws the current location of the ball to the screen
    public void draw(Graphics g){
        g.setColor(Color.black);
        g.fillRect((int) (centerX + 0.5), (int) (centerY + 0.5), raceCar.CAR_LENGTH, raceCar.CAR_WIDTH);
    }

}