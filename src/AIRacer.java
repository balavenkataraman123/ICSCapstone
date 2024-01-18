// used to store the player's data
// ICS Summative, Bala V, Darian Y, ICS4U 2024. LightSpeed racing game.

import java.awt.*;
import java.awt.event.*;

public class AIRacer extends Rectangle{
    // car dimensions are rounded up to nearest

    public Car raceCar;{

    }


    //constructor creates car at given location with given dimensionspublic AIRacer(int x, int y, Car currentCar){
    public void move() {

    }

    //called from GamePanel when any keyboard input is detected
    //updates the position of the car based on user input

    public void draw(Graphics2D g){ // draws car at center location on screen.
        g.drawImage(raceCar.carImage,(int) (600 * GamePanel.scaleMultiplier + 0.5 - raceCar.CAR_WIDTH * GamePanel.pixelsPerMeter/2), (int) (900 * GamePanel.scaleMultiplier + 0.5 - raceCar.CAR_WIDTH * GamePanel.pixelsPerMeter/2), null);
    }

}