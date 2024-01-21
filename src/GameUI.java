import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;


public class GameUI  implements KeyListener {

    public int status;

    public void keyPressed(KeyEvent e){
      if (e.getKeyCode() == 32){
          GamePanel.gameRunning  += 1;
          GamePanel.gameRunning = GamePanel.gameRunning % 6;
      }
    }

    //if a key is released, we'll send it over to the PlayerBall class for processing
    public void keyReleased(KeyEvent e){

    }

    //left empty because we don't need it; must be here because it is required to be overridded by the KeyListener interface
    public void keyTyped(KeyEvent e){

    }

    public void draw(Graphics g) {
        if(GamePanel.gameRunning == 0){// starting splash screen
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(GamePanel.splashScreenBG, 0,0,null);
            g2d.setFont(new Font("Arial", Font.PLAIN, (int) (30*GamePanel.scaleMultiplier)));// sets text font
            // displays information text
            g2d.drawString("Press space to play. UI not complete yet. ", (int) (300* GamePanel.scaleMultiplier), (int) (1090* GamePanel.scaleMultiplier));
            g2d.drawString("W: gas. S:brake; A,D: Steering. R: toggle reverse gear", (int) (250* GamePanel.scaleMultiplier), (int) (1180* GamePanel.scaleMultiplier));
        }
        else if(GamePanel.gameRunning == 1){// race track picking
            g.setColor(Color.white);
            g.fillRect(0,0,(int) (1200 * GamePanel.scaleMultiplier), (int) (1200 * GamePanel.scaleMultiplier));




        }
        else if(GamePanel.gameRunning == 2){ //car picking

        }
        else if(GamePanel.gameRunning == 5){ // end credits.
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(GamePanel.splashScreenBG, 0,0,null);
            g2d.setFont(new Font("Arial", Font.PLAIN, (int) (30*GamePanel.scaleMultiplier)));// sets text font
            // displays information text
            g2d.drawString(GamePanel.endScreenMessage, (int) (300* GamePanel.scaleMultiplier), (int) (1090* GamePanel.scaleMultiplier));
            g2d.drawString("Press space to play again. ", (int) (300* GamePanel.scaleMultiplier), (int) (1130* GamePanel.scaleMultiplier));


        }


    }

}

