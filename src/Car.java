// used to store the Car's data
// ICS Summative, Bala V, Darian Y, ICS4U 2024. LightSpeed racing game.
// reads XML data to generate car configurations
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
    public double maxTireGrip, maxAccelB, maxAccelM, maxBrake, maxSpeed,turnRadius, health;
    public Image carImage;
    public Car(int carID){
        try{ // reads the car's data from XML
            Element car = (Element) GamePanel.carList.item(carID);
            carName = car.getAttribute("id");
            maxTireGrip = parseFloat(car.getElementsByTagName("maxTireGrip").item(0).getTextContent());
            maxAccelM = parseFloat(car.getElementsByTagName("maxAccelM").item(0).getTextContent());
            maxAccelB = parseFloat(car.getElementsByTagName("maxAccelB").item(0).getTextContent());
            maxSpeed = parseFloat(car.getElementsByTagName("maxSpeed").item(0).getTextContent());
            maxBrake = parseFloat(car.getElementsByTagName("maxBrake").item(0).getTextContent());
            turnRadius = parseFloat(car.getElementsByTagName("turnradius").item(0).getTextContent());
            health = parseFloat(car.getElementsByTagName("maxdamage").item(0).getTextContent());
            carImage = Toolkit.getDefaultToolkit().createImage(carName + ".png").getScaledInstance(GamePanel.pixelsPerMeter*CAR_WIDTH, GamePanel.pixelsPerMeter*CAR_LENGTH, Image.SCALE_DEFAULT); // Returns an image of the car scaled to the correct size.
        }
        catch (Exception e) {// in case car data isn't read.
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

    public double calculateAngularVelocity(double speed){ // uses linear interpolation to calculate car's velocity. 
        if(speed <= 0.5){ // If the car is nearly stationary, you can't actually turn it.
            return 0;
        }
        // The turning rate at high speeds depends mainly on whether the tires can keep up with the centripetal force. At lower speeds, the turning speed is limited by the car's geometry.
        // The car turns slower at high speeds, but even at low speeds, it can only take a turn as tight as its turning radius (each car has one).
        // turning circle = 11.5 meters. 11.5 * pi is time to turn 180 degrees. => pi radians in 11.5pi/v seconds -> angular velocity is pi * v / 11.5pi = v/11.5
        // time = distance / speed.
        return(Math.min((speed / turnRadius), (maxTireGrip / speed)));
    }
    public double calculateAcceleration(int direction, double speed){  // uses linear interpolation to calculate car's acceleration.

        if(direction == 1){ // returns acceleration in ms^-2 a the car's speed.
            return ((maxAccelM * speed) + maxAccelB) / 60; // divide by 60 since 60 frames per second
        }
        else{ // car's braking speed. more or less constant.
            return maxBrake/60;
        }
    }

}
