// used to store the Car's data
// ICS Summative, Bala V, Darian Y, ICS4U 2024. LightSpeed racing game.
// Note: Right now, could not get XML reading to work so data is being hard coded to test.
/* These are the import statements for code which is not being used at the moment.
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.image.BufferedImage;

import static java.lang.Float.parseFloat;

*/
import org.w3c.dom.Element;

import java.awt.*;

import static java.lang.Float.parseFloat;

public class Car {
    // Parameters of the car, all are in SI units.  Currently hard coded with the performance data of the Corvette C7 ZR1.
    public String carName;
    public int CAR_LENGTH = 5; // size of the car for rendering and collision physics (pending)
    public int CAR_WIDTH = 2;

    // Acceleration reduces linearly with the speed of the car, so it is almost zero as the car approaches max speed. This is how real cars work.
    // so since it's y=MX+B where X is speed, there is an M and B.
    public double maxTireGrip = 0.8 * 9.81; // this is in ms^-2. Taken from caranddriver.com.
    public double maxAccelB = 10.6264; // This is a linear approximation for the car's acceleration, which decreases as speed goes up. A = MV + B.
    public double maxAccelM = -0.03892;
    // braking strength is generally constant.
    public double maxBrake = 12.346;
    // turning speed is higher in lighter cars like the Mazda RX7 and lower in heavier cars like BMW M5.
    public double maxSpeed = 100; // max speed of the car
    public double turnRadius = 11.5; // turning radius of the car



    public Image carImage;
    public Car(int carID){
        try{
        Element car = (Element) GamePanel.carList.item(carID);
        carName = car.getAttribute("id");
        maxTireGrip = parseFloat(car.getElementsByTagName("maxTireGrip").item(0).getTextContent());
        maxAccelM = parseFloat(car.getElementsByTagName("maxAccelM").item(0).getTextContent());
        maxAccelB = parseFloat(car.getElementsByTagName("maxAccelB").item(0).getTextContent());
        maxSpeed = parseFloat(car.getElementsByTagName("maxSpeed").item(0).getTextContent());
        maxBrake = parseFloat(car.getElementsByTagName("maxBrake").item(0).getTextContent());
        turnRadius = parseFloat(car.getElementsByTagName("turnradius").item(0).getTextContent());
        carImage = Toolkit.getDefaultToolkit().createImage(carName + ".png").getScaledInstance(GamePanel.pixelsPerMeter*CAR_WIDTH, GamePanel.pixelsPerMeter*CAR_LENGTH, Image.SCALE_DEFAULT); // Returns an image of the car scaled to the correct size.
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        }

    // this calculates the turning rate of the car
    // this is the angular velocity such that the friction from the car's tires provides just enough centripetal force to turn the car.
    // the car has a max. acceleration. When it is under linear acceleration, turning ability is limited.
    // centripetal acceleration = v^2 / r, r = v^2 / c.
    // in 180 degrees turn, car travels distance of pi * r.
    // the time taken for this is pi * r / v = pi * v / c
    // this is also the time to turn 180 degrees / pi radians, so the angular velocity is c/v

    public double calculateAngularVelocity(double speed){
        if(speed <= 0.5){ // If the car is nearly stationary, you can't actually turn it.
            return 0;
        }
        // The turning rate at high speeds depends mainly on whether the tires can keep up with the centripetal force. At lower speeds, the turning speed is limited by the car's geometry.
        // The car turns slower at high speeds, but even at low speeds, it can only take a turn as tight as its turning radius (each car has one).
        // turning circle = 11.5 meters. 11.5 * pi is time to turn 180 degrees. => pi radians in 11.5pi/v seconds -> angular velocity is pi * v / 11.5pi = v/11.5
        // time = distance / speed.
        return(Math.min((speed / turnRadius), (maxTireGrip / speed)));
    }
    public double calculateAcceleration(int direction, double speed){  // Increases

        if(direction == 1){ // returns acceleration in ms^-2 a the car's speed.
            return ((maxAccelM * speed) + maxAccelB) / 60; // divide by 60 since 60 frames per second
        }
        else{
            return maxBrake/60;
        }
    }

}
