import java.awt.*;
import java.awt.image.BufferedImage;

public class Car {
    // Parameters of the car. all are in SI units.
    // should be reading from a file, but right now it is hard coded for a Toyota Corolla.
    public int CAR_LENGTH = 5;
    public int CAR_WIDTH = 2;

    // Acceleration reduces linearly with the speed of the car, so it is almost zero as the car approaches max speed. This is how real cars work.
    // so since it's y=MX+B where X is speed, there is an M and B.
    public double maxTireGrip = 0.8 * 9.81; // this is in ms^-2. Taken from caranddriver.com.
    public double maxAccelB;
    public double maxAccelM;
    // braking strength is generally constant.
    public double maxBrake;
    // turning speed is higher in lighter cars like the Mazda RX7 and lower in heavier cars like BMW M5.
    public double maxDamage; // amount of damage car has to sustain to be eliminated. Depends on material used in real life car.
    public double maxSpeed = 100; // max speed of the car
    public double pictureFilePath;

    public Image carImage; //draw off screen
    public Car(){
        carImage = Toolkit.getDefaultToolkit().createImage("corvette.png").getScaledInstance(GamePanel.pixelsPerMeter*CAR_WIDTH, GamePanel.pixelsPerMeter*CAR_LENGTH, Image.SCALE_DEFAULT);
        return;
    }
    // this calculates the turning rate of the car
    // this is the angular velocity such that the friction from the car's tires provides just enough centripetal force to turn the car.
    // the car has a max. acceleration. When it is under linear acceleration, turning ability is limited.
    // centripetal acceleration = v^2 / r, r = v^2 / c.
    // in 180 degrees turn, car travels distance of pi * r.
    // the time taken for this is pi * r / v = pi * v / c
    // this is also the time to turn 180 degrees / pi radians, so the angular velocity is c/v

    public double calculateAngularVelocity(double speed){
        if(speed <= 0.5){
            return 0;
        }
        // turning circle = 11.5 meters. 11.5 * pi is time to turn 180 degrees. => pi radians in 11.5pi/v seconds -> angular velocity is pi * v / 11.5pi = v/11.5
        return(Math.min((speed / 23), (maxTireGrip / speed)));
        //return( maxTireGrip / speed); // can't turn way too fast when the car is slow, cars don't work that way either
    }
    public double calculateAcceleration(double speed){
        return 5.0/60;
    }

}
