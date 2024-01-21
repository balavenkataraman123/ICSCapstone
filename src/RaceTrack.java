// This class is not used for now, as the test racetrack is hard coded in the main method. It will be used for racetrack pre-rendering and mapping for collision detection.
// ICS Summative, Bala V, Darian Y, ICS4U 2024. LightSpeed racing game.

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class RaceTrack {

    public BufferedImage fullImage;
    public String fullImagePath;
    public double mapPPM;

    double last_segment_x = 9999;
    double last_segment_y = 9999;

    public double sx, sy, sa;

    public Image imCache; // cached image for rendering
    public int imheight, imwidth;
    public int numCheckpoints;
    public double trHeight, trWidth;
    public RaceTrack(String trackPath) throws IOException { // constructs the race track and loads its properties from a file.
        File imageFile;
        fullImagePath = trackPath + ".png";
        imageFile = new File(fullImagePath);
        BufferedReader reader = new BufferedReader(new FileReader(trackPath + ".txt"));
        trHeight = parseFloat(reader.readLine());


        fullImage = ImageIO.read(imageFile);
        imheight = fullImage.getHeight(null);
        imwidth = fullImage.getWidth(null);
        mapPPM =  imheight / trHeight;
        trWidth = imwidth/ mapPPM;
        sx = 2*parseFloat(reader.readLine()) / mapPPM;
        sy = 2*parseFloat(reader.readLine()) / mapPPM;
        sa = parseFloat(reader.readLine());
        numCheckpoints = parseInt(reader.readLine());
        reader.close();
    }
    // caches a new track image segment when the user moves outside the old one. Does not render the whole track on user's end for resource use optimizatino.
    public boolean getCurrentTrackSegment(double centerx, double centery) {
        double tl_cornerx, tl_cornery, br_cornerx, br_cornery;
        // this gets an image that is 48 by 48 meters.
        if (Math.max(Math.abs(last_segment_x - centerx), Math.abs(last_segment_y - centery)) > 6) {
            last_segment_y = centery;
            last_segment_x = centerx;
            tl_cornerx = Math.max(0, last_segment_x - 24); // finds location of image
            tl_cornery = Math.max(0, last_segment_y - 24);
            br_cornerx = Math.min(trWidth, last_segment_x + 24);
            br_cornery = Math.min(trHeight, last_segment_y + 24);
            this.imCache = fullImage.getSubimage((int) (tl_cornerx * mapPPM), (int) (tl_cornery * mapPPM), (int) ((br_cornerx - tl_cornerx) * mapPPM), (int) ((br_cornery - tl_cornery)*mapPPM)).getScaledInstance((int) ((br_cornerx - tl_cornerx)*GamePanel.pixelsPerMeter),(int) ((br_cornery - tl_cornery)*GamePanel.pixelsPerMeter), Image.SCALE_FAST);
            return true;


        }
        return false;
    }
    // collision detection and checkpoint detection functions.
    // ideally, there should be multiple "layers", or images, in the track. One is "rendering layer" which is the image that is cropped and rendereed to the user, and one is the location layer which is used to locate all the key points of the track by color coding
    // here, the rendering layer is used for both, which works for the prototype.
    public int HasCrashed(double centerX, double centerY, double carAngle){ // checks if the user has crashed.
        double[]corners_x = {- 1,  1, 1,  -1}; // relative location of all the points of the car
        double[]corners_y = {-2.5, -2.5, 2.5, 2.5};
        double caSin = Math.sin(carAngle);
        double caCos = Math.cos(carAngle);
        double currentX, currentY;
        int crashes = 0;


        // applies the transformation on all the corners. matrix is
        // [sin theta  -cos theta]
        // [cos theta   sin theta]
        // this is applied on all 4 corners of the car to see which one hits the wall, and bounce the car accordingly.
        for(int i = 0; i < 4; i++){
            currentX = corners_x[i] * caCos - corners_y[i] * caSin + centerX; // sin and cosine are switched as angle is with vertical not horizontal.
            currentY = corners_x[i] * caSin + corners_y[i] * caCos + centerY;
            int color = fullImage.getRGB((int) (currentX * mapPPM), (int) (currentY * mapPPM)); // gets map color and RGB values
            int blue = color & 0xff; // reads all the pixel values from the image's pixel.
            int green = (color & 0xff00) >> 8;
            int red = (color & 0xff0000) >> 16;
            if(red+blue+green <= 60){
                crashes = crashes | 1 << i; // stores the corner of the car that got hit, by setting that bit to
            }
        }
        return crashes;
    }
    public boolean PassedCheckpoint(double centerX, double centerY){ // checks if user has passed the check point.
        int color = fullImage.getRGB((int) (centerX * mapPPM), (int) (centerY * mapPPM)); // gets map color and RGB values and sees if it is the check point.
        int blue = color & 0xff;
        int green = (color & 0xff00) >> 8;
        int red = (color & 0xff0000) >> 16;
        if(red+blue+green >= 650){
            return  true;
        }
        return false;
    }
    public Image getMiniMap(){
        return Toolkit.getDefaultToolkit().createImage(fullImagePath).getScaledInstance((int) (200 * GamePanel.scaleMultiplier), (int) (200 * GamePanel.scaleMultiplier), Image.SCALE_DEFAULT);
    }

    public void checkPositions(RaceCompetitor player) {
    }
}
