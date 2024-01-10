/* GamePanel class acts as the main "game loop" - continuously runs the game and calls whatever needs to be called

Child of JPanel because JPanel contains methods for drawing to the screen

Implements KeyListener interface to listen for keyboard input

Implements Runnable interface to use "threading" - let the game do two things at once

*/
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;

public class GamePanel extends JPanel implements Runnable, KeyListener{

    //dimensions of window
    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 1200;
    public static int pixelsPerMeter = 50;

    public Thread gameThread;
    public Image image = Toolkit.getDefaultToolkit().createImage("raceTrack.png").getScaledInstance(8000, 8000, Image.SCALE_DEFAULT); //draw off screen

    public Image minimap = Toolkit.getDefaultToolkit().createImage("raceTrack.png").getScaledInstance(200, 200, Image.SCALE_DEFAULT); //draw off screen

    public Graphics graphics;
    public RaceCompetitor ball;


    public GamePanel(){
        Car raceCar = new Car();
        ball = new RaceCompetitor(80, 80, raceCar); //create a player controlled ball, set start location to middle of screen
        this.setFocusable(true); //make everything in this class appear on the screen
        this.addKeyListener(this); //start listening for keyboard input

        //add the MousePressed method from the MouseAdapter - by doing this we can listen for mouse input. We do this differently from the KeyListener because MouseAdapter has SEVEN mandatory methods - we only need one of them, and we don't want to make 6 empty methods
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

        //make this class run at the same time as other classes (without this each class would "pause" while another class runs). By using threading we can remove lag, and also allows us to do features like display timers in real time!
        gameThread = new Thread(this);
        gameThread.start();
    }

    //paint is a method in java.awt library that we are overriding. It is a special method - it is called automatically in the background in order to update what appears in the window. You NEVER call paint() yourself
    public void paint(Graphics g){
        int centerx = (int) (ball.centerX * pixelsPerMeter + 0.5); // location of the car on the track
        int centery = (int) (ball.centerY * pixelsPerMeter + 0.5);
        // car's center coordinates are (360,360). Right now, it is 800,800. Image is moved by (360 - 800, 360-800)

        AffineTransform affineTransform = AffineTransform.getTranslateInstance((600-centerx), (900-centery));
        affineTransform.rotate(-ball.carAngle, centerx, centery);

        //we are using "double buffering here" - if we draw images directly onto the screen, it takes time and the human eye can actually notice flashes of lag as each pixel on the screen is drawn one at a time. Instead, we are going to draw images OFF the screen, then simply move the image on screen as needed.
        //graphics = image.getGraphics();
        //draw(graphics);//update the positions of everything on the screen

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, affineTransform, null);
        g2d.drawImage(minimap, 20,880,null);
        g2d.setColor(Color.black);
        g2d.fillOval(centerx/40 + 20 - 5, centery/40+880 - 5,10,10);
        ball.draw(g2d);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString("Minimap", 20,850);
        g2d.drawString("(C) 2024, Subpixel Studios",20,1180);
        g2d.drawString("Speed: " + (int) (ball.forwardSpeed * 2.2) + "mph",20,20);


    }

    //call the draw methods in each class to update positions as things move
    public void draw(Graphics g){
    }

    //call the move methods in other classes to update positions
    //this method is constantly called from run(). By doing this, movements appear fluid and natural. If we take this out the movements appear sluggish and laggy
    public void move(){
        ball.move();
    }

    //handles all collision detection and responds accordingly

    //run() method is what makes the game continue running without end. It calls other methods to move objects,  check for collision, and update the screen
    public void run(){
        //the CPU runs our game code too quickly - we need to slow it down! The following lines of code "force" the computer to get stuck in a loop for short intervals between calling other methods to update the screen.
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
                System.out.println(delta);
                move();
                repaint();
                delta--;
            }
        }
    }

    //if a key is pressed, we'll send it over to the PlayerBall class for processing
    public void keyPressed(KeyEvent e){
        ball.keyPressed(e);
    }

    //if a key is released, we'll send it over to the PlayerBall class for processing
    public void keyReleased(KeyEvent e){
        ball.keyReleased(e);
    }

    //left empty because we don't need it; must be here because it is required to be overridded by the KeyListener interface
    public void keyTyped(KeyEvent e){

    }
}
