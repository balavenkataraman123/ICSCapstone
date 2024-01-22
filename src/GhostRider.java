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
    public BufferedReader reader; // file reader to update position from file
    public double centerX, centerY, carAngle; // position of car
    public Image carImg;
    public boolean finished_race = false; // determines whether player or ghost wins.

    public GhostRider(String filePath){
        try { // reads file so it can draw the car frame-by-frame at the coordinates it was at.
            reader = new BufferedReader(new FileReader(filePath)); // file reader
            String line = reader.readLine();
            raceCar = new Car(parseInt(line)); // finds the car which was used to draw its image.
            carImg = raceCar.carImage.getScaledInstance(2*GamePanel.pixelsPerMeter, 5*GamePanel.pixelsPerMeter, Image.SCALE_DEFAULT);
        }
        catch (Exception e){ // in case lap benchmark has not been set, in first playthrough.
            System.out.println("No ghost drivers yet. You will become the ghost driver.");
        }
    }


    public void move() { // moves the ghost to the next location, reading the file. Finishes it if the ghost has crossed the finish line.
        if(!finished_race){ // if ghost hasnt already crossed finish line
        try { // reads the next line and updates position of the ghost.
            String line = reader.readLine();
            if(line == null){ // in case file is over: ghost has crossed the finish line.
                finished_race = true;
                reader.close(); // closes file reader so no memory leak
                return;
            }
            String[] floats = line.split(" ");
            centerX = parseFloat(floats[0]); // updates position by reading from file.
            centerY = parseFloat(floats[1]);
            carAngle = parseFloat(floats[2]);
        }
        catch(Exception e){
        }
        }

    }


}