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
    public Image getCurrentTrackSegment(double centerx, double centery){
        return TrackTopDown.get(0).get(0);
    }
}
