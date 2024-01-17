// This class is not used for now, as the test racetrack is hard coded in the main method. It will be used for racetrack pre-rendering and mapping for collision detection.
// ICS Summative, Bala V, Darian Y, ICS4U 2024. LightSpeed racing game.
// This class is unused and not finished, please ignore.
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RaceTrack {
    public ArrayList<ArrayList<Image>> TrackTopDown;
    public BufferedImage fullImage;
    public String fullImagePath;
    public double mapPPM;

    double last_segment_x = 9999;
    double last_segment_y = 9999;


    public double trHeight, trWidth;
    public RaceTrack(String trackPath) throws IOException {
        File imageFile;
        double imheight;
        fullImagePath = trackPath + ".png";
        imageFile = new File(fullImagePath);
        fullImage = ImageIO.read(imageFile);
        imheight = fullImage.getHeight(null);
        trHeight = 4200;
        trWidth = 5500;
        mapPPM =  imheight / trHeight;
    }
    // plans
    // break the track up into segments, with each of them being 200 x 200 m. Supersample images on demand. If the image location is same as previous, return zero. otherwise, return the image.
    // make a function to get the track's image given that
    public BufferedImage getCurrentTrackSegment(double centerx, double centery){
        // this gets an image that is 48 by 48 meters.
        if(Math.max(Math.abs(last_segment_x - centerx),Math.abs(last_segment_y - centery)) > 12){
            last_segment_y = centery;
            last_segment_x = centerx;
            // take an image which is 48m x 48m worth.

        }
        return null;

    }
    // collision detection function
    public double onTrack(double centerX, double centerY, double carAngle){
        int color = fullImage.getRGB((int) (centerX * mapPPM), (int) (centerY * mapPPM)); // gets map color and RGB values
        int blue = color & 0xff;
        int green = (color & 0xff00) >> 8;
        int red = (color & 0xff0000) >> 16;
        System.out.println(red + " " + green + " " + blue);
        return 0;

    }
    public Image getMiniMap(){
        return Toolkit.getDefaultToolkit().createImage(fullImagePath).getScaledInstance((int) (200 * GamePanel.scaleMultiplier), (int) (200 * GamePanel.scaleMultiplier), Image.SCALE_DEFAULT);
    }

    public void checkPositions(RaceCompetitor player) {
    }
}
