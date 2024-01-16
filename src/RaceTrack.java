// This class is not used for now, as the test racetrack is hard coded in the main method. It will be used for racetrack pre-rendering and mapping for collision detection.
// ICS Summative, Bala V, Darian Y, ICS4U 2024. LightSpeed racing game.
// This class is unused and not finished, please ignore.
import java.awt.*;
import java.util.ArrayList;

public class RaceTrack {
    public ArrayList<ArrayList<Image>> TrackTopDown;
    public Image fullImage;
    public int numTrackFrames;
    public RaceTrack(String trackPath){
        fullImage = null;
    }
    // plans
    // break the track up into segments, with each of them being 200 x 200 m. Supersample images on demand. If the image location is same as previous, return zero. otherwise, return the image.

    // make a function to get the track's image given that
    public Image getCurrentTrackSegment(double centerx, double centery){
        return TrackTopDown.get(0).get(0);
    }
    // collision detection function
    public double isColliding(){
        return 0;
    }
}
