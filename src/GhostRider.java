// used to store the player's data
// ICS Summative, Bala V, Darian Y, ICS4U 2024. LightSpeed racing game.

import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class GhostRider extends Rectangle{
    public Car raceCar;
    public BufferedReader reader;
    public double centerX, centerY, carAngle;
    public Image carImg;
    public boolean finished_race = false;
    public GhostRider(String filePath){
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            raceCar = new Car(parseInt(line));
            carImg = raceCar.carImage.getScaledInstance(2*GamePanel.pixelsPerMeter, 5*GamePanel.pixelsPerMeter, Image.SCALE_DEFAULT);
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("AI car not working today");
        }
    }



    //constructor creates car at given location
    public void move() {
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

    public void draw(Graphics2D g){ // draws car at center location on screen.
        g.drawImage(raceCar.carImage,(int) (600 * GamePanel.scaleMultiplier + 0.5 - raceCar.CAR_WIDTH * GamePanel.pixelsPerMeter/2), (int) (900 * GamePanel.scaleMultiplier + 0.5 - raceCar.CAR_WIDTH * GamePanel.pixelsPerMeter/2), null);
    }

}