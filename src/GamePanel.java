/* GamePanel class acts as the main "game loop" - continuously runs the game and calls whatever needs to be called

Child of JPanel because JPanel contains methods for drawing to the screen

Implements KeyListener interface to listen for keyboard input

Implements Runnable interface to use "threading" - let the game do two things at once

ICS Summative, Bala V, Darian Y, ICS4U 2024. LightSpeed racing game.

*/
import org.w3c.dom.NodeList;

import java.awt.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener{
    public static double scaleMultiplier = 0.5 ; // scale factor to adjust for lower or higher resolution screens. 0.5 works for 1366x768 displays.

    //dimensions of window
    public static final int GAME_WIDTH = (int) (1200 * scaleMultiplier); // game resolution
    public static final int GAME_HEIGHT = (int) ( 1200 * scaleMultiplier);
    public static int pixelsPerMeter = (int) (50 * scaleMultiplier); // display scaling
    public static NodeList carList; // car data
    public static ArrayList<String> trackList = new ArrayList(); // track file names
    public static int gameRunning = 0; // game state
    public static int chosencarID = 1; // car to be used in race

    public static int chosenTrackIndex = 0; // track to be used in race
    public static String chosenTrack = ""; // name of track to be used in race
    public static String endScreenMessage = ""; // message to player
    public double TLXmeters, TLYmeters; // rendering location
    public Thread gameThread; // thread to run game
    public static Image splashScreenBG = Toolkit.getDefaultToolkit().createImage("splashscreen.png").getScaledInstance((int) (1200 * scaleMultiplier), (int) (1200 * scaleMultiplier), Image.SCALE_DEFAULT); // background image of the splash screen.
    public Image minimap; // track map

    public Clip introSong; // Audio file to play start screen music.

    public boolean musicWorks; // verifies whether audio component is working.

    public RaceCompetitor player; // player object

    public GhostRider ghost; // competition for the player - the "ghost" that drove last time.

    public RaceTrack raceTrack; // racetrack object

    public GameUI gui = new GameUI(); // game UI renderer

    public GamePanel(){
        File curDir =  new File("./"); // set this file path.
        // the function to start the game
        File audioFile; // loads the game music
        AudioInputStream audioStream;
        String fname;

        try{ // loads the car files and the track files.
            carList = XMLReader.readXMLDocumentFromFile("cars.XML").getElementsByTagName("car");
            // reads all the files in the track directory.
            for(File f : curDir.listFiles()){
                fname = f.getName();
                if(fname.endsWith(".txt") && !fname.endsWith("Ghost.txt")){ // reads any text file in the working directory which is not a path log file. These are track files.
                    trackList.add(fname.substring(0,fname.length() - 4)); // Stores the names of the tracks.
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("failed to load car/or track data. closing program");
            System.exit(1);
        }
        try{ // tries to load an audio file
            audioFile = new File("introsong.wav");
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
        AffineTransform mapTransform; // matrix transformations used in rendering.
        AffineTransform ghostTransform;
        // this program crops small image segments from the track image and renders them to preserve memory. this code is for that.
        boolean imChanged;
        Graphics2D g2d = (Graphics2D) g;
        imChanged = raceTrack.getCurrentTrackSegment(player.centerX, player.centerY); // reloads cache image from track class if player moves out of bounds

        if(imChanged){ // sets new image segment location if cache image changes
            TLXmeters = Math.max(0, player.centerX - 24);
            TLYmeters = Math.max(0, player.centerY - 24);
        }
        // main graphics code
        double gcx = 600*scaleMultiplier + (ghost.centerX - player.centerX) * pixelsPerMeter; // change in x and y coordinate for ghost car.
        double gcy = 900*scaleMultiplier + (ghost.centerY - player.centerY) * pixelsPerMeter;
        ghostTransform = AffineTransform.getTranslateInstance(gcx,gcy); // adds translation to matrix transformation
        mapTransform = AffineTransform.getTranslateInstance((600*scaleMultiplier - pixelsPerMeter*(player.centerX - TLXmeters)), (900*scaleMultiplier - pixelsPerMeter*(player.centerY - TLYmeters))); // shifts the track so that the car is located in the middle of the screen, closer to the bottom.
        mapTransform.rotate(-player.carAngle, pixelsPerMeter*(player.centerX - TLXmeters), pixelsPerMeter*(player.centerY - TLYmeters));// applies a rotational matrix transformation on the track image so it turns along with the car.
        ghostTransform.rotate(-player.carAngle, pixelsPerMeter*(player.centerX - ghost.centerX),pixelsPerMeter*(player.centerY - ghost.centerY));// applies a rotational matrix transformation on the track image so it turns along with the car.
        ghostTransform.rotate(ghost.carAngle); // make the ghost car turn in its direction
        g2d.drawImage(raceTrack.imCache, mapTransform, null); // draws the track image with the matrix transformation applied. Will look into using SIMD to improve maximum frame rate.
        g2d.drawImage(ghost.carImg, ghostTransform, null); // draws the ghost car image with the matrix transformation applied. Will look into using SIMD to improve maximum frame rate.

        // code which draws the mini map.
        g2d.drawImage(minimap, (int) (20*scaleMultiplier),(int)(880 * scaleMultiplier),null); // draws map image
        g2d.setColor(Color.black);
        // draws player location as black dot
        g2d.fillOval( (int)((player.centerX / raceTrack.trWidth) * 200 * scaleMultiplier +  (20*scaleMultiplier) - 5),  (int)((player.centerY / raceTrack.trHeight) * 200 * scaleMultiplier +  (880*scaleMultiplier) - 5),10,10); // draws the car on the map
        g2d.setColor(Color.white);
        // draws ghost location as a white dot
        g2d.fillOval( (int)((ghost.centerX / raceTrack.trWidth) * 200 * scaleMultiplier +  (20*scaleMultiplier) - 5),  (int)((ghost.centerY / raceTrack.trHeight) * 200 * scaleMultiplier +  (880*scaleMultiplier) - 5),10,10); // draws the car on the map
        // draws text UI elements
        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (30*scaleMultiplier)));// sets text font
        g2d.drawString("(C) 2024, Subpixel Studios",20,(int) (int) (1170*scaleMultiplier));
        g2d.drawString("Speed: " + (int) (player.forwardSpeed * 2.2 * 1.61) + "kmph",20,20);
        g2d.drawString("Remaining car health: " + (int) (100 * player.health / player.raceCar.health) + "%",20,60);
        g2d.drawString("Remaining checkpoints: " + (raceTrack.numCheckpoints - player.numCH),20,100);

        player.draw(g2d); // draw player's car on top of everything.
    }

    public void paintSplashScreen(Graphics g){ // draws the pre game UI
        //gui.configure(gameRunning);;
        gui.draw(g);
    }

    //paint is a method in java.awt library that we are overriding. It is a special method - it is called automatically in the background in order to update what appears in the window. You NEVER call paint() yourself
    public void paint(Graphics g){ // will call different paint functions depending on whether the game is running or not.
        if(gameRunning == 3) {
            startGame();
        }
        else if(gameRunning == 4){
            paintGame(g);
        }
        else{
            paintSplashScreen(g);
        }

    }

    //Updates the position of the car and checks if it is crashed.
    public void move(){
        int crashes;
        if(gameRunning == 4) { // only does it if the game is running
            player.move(); // moves the player
            ghost.move();// switches ghost to next position snapshot
            crashes = raceTrack.HasCrashed(player.centerX,player.centerY, player.carAngle); // sides which the player has crashed in
            player.bounce(crashes); // update player with crash physics
            if(player.health < 0){ // if player wrecks the car
                endScreenMessage = "You totalled your car";
                gameRunning = 5;
            }
            if(raceTrack.PassedCheckpoint(player.centerX, player.centerY)){ // verifies checkpoint progress
                player.verifyCheckPoint(); // checks if the checkpoint is not repeated
                if(player.numCH > raceTrack.numCheckpoints){ // if player has finished the race
                    if(ghost.finished_race){ // if ghost finishes race before player
                        endScreenMessage = "You finished the race. Ghost wins"; // updates end screen message
                    }
                    else{ // if ghost is not finished yet
                        endScreenMessage = "You finished the race, You win. You are now the ghost.";
                        player.writeCoords();
                    }
                    gameRunning = 5; // changes game state to end condition
                }
            }
        }
    }

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
    public void startGame(){ // called to start the game
        chosenTrack = trackList.get(chosenTrackIndex); // sets track, player, and ghost variables.
        try {
            raceTrack = new RaceTrack(chosenTrack);
            minimap = raceTrack.getMiniMap();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Could not load the race track file. ");
            System.exit(1);
        }
        player = new RaceCompetitor(raceTrack.sx, raceTrack.sy, raceTrack.sa, chosencarID, chosenTrack); // places player on start line of the track
        ghost = new GhostRider(chosenTrack + "_Ghost.txt"); // initializes the ghost
        if(musicWorks){ // stops the intro song for the game
            introSong.stop();
        }
        gameRunning = 4;  // starts the game and stops the music when space is pressed.
    }

    // keypress handler
    public void keyPressed(KeyEvent e){
        if(gameRunning == 4) {
            player.keyPressed(e); // player handles key controls when the game is running
        }
        else{
            gui.keyPressed(e); // if not GUI handles it.
        }
    }

    // key release handler
    public void keyReleased(KeyEvent e){
        if(gameRunning == 4) { // handled by player if game is running and UI otherwise.
            player.keyReleased(e);
        }
    }

    //left empty because we don't need it; must be here because it is required to be overridded by the KeyListener interface
    public void keyTyped(KeyEvent e){

    }
}
