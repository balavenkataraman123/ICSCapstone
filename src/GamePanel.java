/* GamePanel class acts as the main "game loop" - continuously runs the game and calls whatever needs to be called

Child of JPanel because JPanel contains methods for drawing to the screen

Implements KeyListener interface to listen for keyboard input

Implements Runnable interface to use "threading" - let the game do two things at once

ICS Summative, Bala V, Darian Y, ICS4U 2024. LightSpeed racing game.

*/
import org.w3c.dom.NodeList;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

public class GamePanel extends JPanel implements Runnable, KeyListener{
    public static double scaleMultiplier = 1; // scale factor to adjust for lower or higher resolution screens. 0.5 works for 1366x768 displays.

    //dimensions of window
    public static final int GAME_WIDTH = (int) (1200 * scaleMultiplier); // game resolution
    public static final int GAME_HEIGHT = (int) ( 1200 * scaleMultiplier);
    public static int pixelsPerMeter = (int) (50 * scaleMultiplier); // display scaling
    public static NodeList carList;
    public static NodeList trackList;

    public double TLXmeters, TLYmeters;

    public Thread gameThread;
    public Image splashScreenBG = Toolkit.getDefaultToolkit().createImage("splashscreen.png").getScaledInstance((int) (1200 * scaleMultiplier), (int) (1200 * scaleMultiplier), Image.SCALE_DEFAULT); // background image of the splash screen.
    public Image image;
    public Image minimap;

    public Clip introSong; // Audio file to play start screen music.

    public boolean musicWorks; // verifies whether audio component is working.

    public RaceCompetitor player; // player object

    public RaceTrack raceTrack; // racetrack object

    public boolean gameRunning = false;

    public int chosencarID = 2;

    public int chosentrackID = 0;

    public int[] nextcheckpoint;


    public GamePanel(){
        // the function to start the game
        File audioFile; // loads the game music
        AudioInputStream audioStream;
        try{
            carList = XMLReader.readXMLDocumentFromFile("cars.XML").getElementsByTagName("car");
            //trackList = XMLReader.readXMLDocumentFromFile("tracks.XML").getElementsByTagName("track");
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("failed to load car data. closing program");
            System.exit(1);
        }
        try{ // tries to load an audio file
            audioFile = new File("tokyodriftsong.wav");
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            // Create a clip
            introSong = AudioSystem.getClip();
            introSong.open(audioStream);
            introSong.start();
            musicWorks = true;

        }
        catch(Exception e){ // works anyway if audio file can't load
            e.printStackTrace();
            System.out.println("Sound does not work now. Proceeding to run game anyway");
        }

        this.setFocusable(true); //make everything in this class appear on the screen
        this.addKeyListener(this); //start listening for keyboard input

        //add the MousePressed method from the MouseAdapter - by doing this we can listen for mouse input. We do this differently from the KeyListener because MouseAdapter has SEVEN mandatory methods - we only need one of them, and we don't want to make 6 empty methods
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

        //make this class run at the same time as other classes (without this each class would "pause" while another class runs). By using threading we can remove lag, and also allows us to do features like display timers in real time!
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void paintGame(Graphics g){ // paint class, for if the game is running
        AffineTransform mapTransform;
        boolean imChanged;
        Graphics2D g2d = (Graphics2D) g;
        imChanged = raceTrack.getCurrentTrackSegment(player.centerX, player.centerY);

        if(imChanged){
            TLXmeters = Math.max(0, player.centerX - 24);
            TLYmeters = Math.max(0, player.centerY - 24);
        }

        mapTransform = AffineTransform.getTranslateInstance((600*scaleMultiplier - pixelsPerMeter*(player.centerX - TLXmeters)), (900*scaleMultiplier - pixelsPerMeter*(player.centerY - TLYmeters))); // shifts the track so that the car is located in the middle of the screen, closer to the bottom.
        mapTransform.rotate(-player.carAngle, pixelsPerMeter*(player.centerX - TLXmeters), pixelsPerMeter*(player.centerY - TLYmeters));// applies a rotational matrix transformation on the track image so it turns along with the car.
        g2d.drawImage(raceTrack.imCache, mapTransform, null); // draws the track image with the matrix transformation applied. Will look into using SIMD to improve maximum frame rate.
        // code which draws the mini map. might not work well when scale is changed. need to fix, so its commented.
        g2d.drawImage(minimap, (int) (20*scaleMultiplier),(int)(880 * scaleMultiplier),null);
        g2d.setColor(Color.black);
        g2d.fillOval( (int)((player.centerX / raceTrack.trWidth) * 200 * scaleMultiplier +  (20*scaleMultiplier) - 5),  (int)((player.centerY / raceTrack.trHeight) * 200 * scaleMultiplier +  (880*scaleMultiplier) - 5),10,10); // draws the car on the map
        // draws text UI elements
        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (20*scaleMultiplier)));// sets text font
        //g2d.drawString("Minimap", 20,850);
        g2d.drawString("(C) 2024, Subpixel Studios",20,(int) (int) (1180*scaleMultiplier));
        g2d.drawString("Speed: " + (int) (player.forwardSpeed * 2.2) + "mph",20,20);



        player.draw(g2d);
        raceTrack.checkPositions(player);
    }

    public void paintSplashScreen(Graphics g){ // draws the pre game UI
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(splashScreenBG, 0,0,null);
        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (20*scaleMultiplier)));// sets text font
        // displays information text
        g2d.drawString("Press space to play. UI not complete yet. ", (int) (500*scaleMultiplier), (int) (1090*scaleMultiplier));
        g2d.drawString("And yes, this is a photoshopped version of the Tokyo Drift movie poster. ", (int) (250*scaleMultiplier), (int) (1120*scaleMultiplier));
        g2d.drawString("Song is from the movie as well. Both subject to change.", (int) (250*scaleMultiplier), (int) (1150*scaleMultiplier));
        g2d.drawString("W: gas. S:brake; A,S: Steering. R: toggle reverse gear", (int) (250*scaleMultiplier), (int) (1180*scaleMultiplier));


    }

    //paint is a method in java.awt library that we are overriding. It is a special method - it is called automatically in the background in order to update what appears in the window. You NEVER call paint() yourself
    public void paint(Graphics g){ // will call different paint functions depending on whether the game is running or not.
        if(gameRunning){
            paintGame(g);
        }
        else{
            paintSplashScreen(g);
        }

    }

    //call the move methods in other classes to update positions
    //this method is constantly called from run(). By doing this, movements appear fluid and natural. If we take this out the movements appear sluggish and laggy
    public void move(){ // updates the car position
        int crashes;
        if(gameRunning) {
            player.move();
            crashes = raceTrack.onTrack(player.centerX,player.centerY, player.carAngle);
            player.bounce(crashes);

        }
    }

    //handles all collision detection and responds accordingly

    //run() method is what makes the game continue running without end. It calls other methods to move objects,  check for collision, and update the screen
    public void run(){
        //the CPU runs our game code too quickly - we need to slow it down! The following lines of code "force" the computer to get stuck in a loop for short intervals between calling other methods to update the screen.
        // This is original code from the graphics template code. Will modify it to be able to select refresh rate of 60hz, 120hz, 144hz, etc. in a GUI settings menu.
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000/amountOfTicks;
        double delta = 0;
        long now;

        while(true){ //this is the infinite game loop
            now = System.nanoTime();
            delta = delta + (now-lastTime)/ns;
            lastTime = now;

            //only move objects around and update screen if enough time has passed
            if(delta >= 1){
                move();
                repaint();
                delta--;
            }
        }
    }
    public void startGame(){
        player = new RaceCompetitor(273, 142, 1.2,new Car(chosencarID));
        try {
            raceTrack = new RaceTrack("RaceTrack1");
            minimap = raceTrack.getMiniMap();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Could not load the race track file. ");
            System.exit(1);
        }
        if(musicWorks){
            introSong.stop();
        }
        gameRunning = true;  // starts the game and stops the music when space is pressed.

    }

    //if a key is pressed, we'll send it over to the PlayerBall class for processing
    public void keyPressed(KeyEvent e){
        if(gameRunning) {
            player.keyPressed(e);
        }
        else{
            if(e.getKeyCode()== 32){ // CHANGE THIS TO 49 FOR RUNNING ON MACOS
                startGame();
            }
        }
    }

    //if a key is released, we'll send it over to the PlayerBall class for processing
    public void keyReleased(KeyEvent e){
        if(gameRunning) {
            player.keyReleased(e);
        }
    }

    //left empty because we don't need it; must be here because it is required to be overridded by the KeyListener interface
    public void keyTyped(KeyEvent e){

    }
}
