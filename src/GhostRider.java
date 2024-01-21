// used to store the data of the "ghost" : The previous player that played this game
// if you manage to beat the ghost in the race, the ghost becomes you. Pass around the game files to determine the best player in the class.
// ICS Summative, Bala V, Darian Y, ICS4U 2024. LightSpeed racing game.

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class GhostRider extends Rectangle{
    public Car raceCar; // car variables
    public BufferedReader reader;
    public double centerX, centerY, carAngle;
    public Image carImg;
    public boolean finished_race = false; // determines whether player or ghost wins.

    public GhostRider(String filePath){
        try { // draws the car frame-by-frame at the coordinates it was at.
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            raceCar = new Car(parseInt(line));
            carImg = raceCar.carImage.getScaledInstance(2*GamePanel.pixelsPerMeter, 5*GamePanel.pixelsPerMeter, Image.SCALE_DEFAULT);
        }
        catch (Exception e){
            System.out.println("No ghost drivers yet. You will become the ghost driver.");
        }
    }


    public void move() { // moves the ghost to the next location, reading the file. Finishes it if the ghost has crossed the finish line.
        if(!finished_race){
        try {
            String line = reader.readLine();
            if(line == null){
                finished_race = true;
                reader.close();
                return;
            }
            String[] floats = line.split(" ");
            centerX = parseFloat(floats[0]);
            centerY = parseFloat(floats[1]);
            carAngle = parseFloat(floats[2]);
        }
        catch(Exception e){
        }
        }

    }

    //called from GamePanel when any keyboard input is detected
    //updates the position of the car based on user input


}