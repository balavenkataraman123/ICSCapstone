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

    public Image imCache;
    public int imheight, imwidth;

    public double trHeight, trWidth;
    public RaceTrack(String trackPath) throws IOException {
        File imageFile;
        fullImagePath = trackPath + ".png";
        imageFile = new File(fullImagePath);
        fullImage = ImageIO.read(imageFile);
        imheight = fullImage.getHeight(null);
        imwidth = fullImage.getWidth(null);
        trHeight = 330;
        mapPPM =  imheight / trHeight;
        trWidth = imwidth/ mapPPM;
    }
    // plans
    // break the track up into segments, with each of them being 200 x 200 m. Supersample images on demand. If the image location is same as previous, return zero. otherwise, return the image.
    // make a function to get the track's image given that
    public boolean getCurrentTrackSegment(double centerx, double centery) {// throws Exception {
        double tl_cornerx, tl_cornery, br_cornerx, br_cornery;
        // this gets an image that is 48 by 48 meters.
        if (Math.max(Math.abs(last_segment_x - centerx), Math.abs(last_segment_y - centery)) > 6) {
            last_segment_y = centery;
            last_segment_x = centerx;
            tl_cornerx = Math.max(0, last_segment_x - 24);
            tl_cornery = Math.max(0, last_segment_y - 24);
            br_cornerx = Math.min(trWidth, last_segment_x + 24);
            br_cornery = Math.min(trHeight, last_segment_y + 24);
            // take an image which is 48m x 48m worth.
            this.imCache = fullImage.getSubimage((int) (tl_cornerx * mapPPM), (int) (tl_cornery * mapPPM), (int) ((br_cornerx - tl_cornerx) * mapPPM), (int) ((br_cornery - tl_cornery)*mapPPM)).getScaledInstance((int) ((br_cornerx - tl_cornerx)*GamePanel.pixelsPerMeter),(int) ((br_cornery - tl_cornery)*GamePanel.pixelsPerMeter), Image.SCALE_FAST);
            return true;


        }
        return false;
    }
    // collision detection function
    public int onTrack(double centerX, double centerY, double carAngle){
        double[]corners_x = {- 1,  1, 1,  -1};
        double[]corners_y = {-2.5, -2.5, 2.5, 2.5};
        double caSin = Math.sin(carAngle);
        double caCos = Math.cos(carAngle);
        double currentX, currentY;
        boolean crashed = false;
        int crashes = 0;


        // applies the affine transformation on all the corners. matrix is
        // [sin theta - cos theta]
        // [cos theta   sin theta]
        for(int i = 0; i < 4; i++){
            currentX = corners_x[i] * caCos - corners_y[i] * caSin + centerX;
            currentY = corners_x[i] * caSin + corners_y[i] * caCos + centerY;
            int color = fullImage.getRGB((int) (currentX * mapPPM), (int) (currentY * mapPPM)); // gets map color and RGB values
            int blue = color & 0xff;
            int green = (color & 0xff00) >> 8;
            int red = (color & 0xff0000) >> 16;
            if(red+blue+green <= 60){
                crashes = crashes | 1 << i; // stores the corner of the car that got hit.
            }
        }
        return crashes;

    }
    public Image getMiniMap(){
        return Toolkit.getDefaultToolkit().createImage(fullImagePath).getScaledInstance((int) (200 * GamePanel.scaleMultiplier), (int) (200 * GamePanel.scaleMultiplier), Image.SCALE_DEFAULT);
    }

    public void checkPositions(RaceCompetitor player) {
    }
}
