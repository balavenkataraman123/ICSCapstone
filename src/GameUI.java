// used to select the car and the racetrack
// ICS Summative, Bala V, Darian Y, ICS4U 2024. LightSpeed racing game.

import org.w3c.dom.Element;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;


public class GameUI {

    public Image trackimage; // images for car selection screen
    public Image carImage;

    public void keyPressed(KeyEvent e){ // changes car or track selection on key press
        if (e.getKeyCode() == 32){
            org.w3c.dom.Element car;
            GamePanel.gameRunning  += 1;
            GamePanel.gameRunning = GamePanel.gameRunning % 6;

            car = (org.w3c.dom.Element) GamePanel.carList.item(GamePanel.chosencarID); // updates car and track when selection interface is loaded
            carImage = Toolkit.getDefaultToolkit().createImage(car.getAttribute("id") + "icon.jpg").getScaledInstance((int) (1000 * GamePanel.scaleMultiplier), (int) (600 * GamePanel.scaleMultiplier), Image.SCALE_DEFAULT); // this is car's dipslay image, not race image.
            trackimage = Toolkit.getDefaultToolkit().createImage(GamePanel.trackList.get(GamePanel.chosenTrackIndex) + ".png").getScaledInstance((int) (600 * GamePanel.scaleMultiplier), (int) (600 * GamePanel.scaleMultiplier), Image.SCALE_DEFAULT);
        }
        if(GamePanel.gameRunning == 1){ // select track
            if (e.getKeyCode() == 37){
                GamePanel.chosenTrackIndex -= 1;
                GamePanel.chosenTrackIndex += GamePanel.trackList.size();
                GamePanel.chosenTrackIndex  = GamePanel.chosenTrackIndex % GamePanel.trackList.size();
                // update image
                trackimage = Toolkit.getDefaultToolkit().createImage(GamePanel.trackList.get(GamePanel.chosenTrackIndex) + ".png").getScaledInstance((int) (600 * GamePanel.scaleMultiplier), (int) (600 * GamePanel.scaleMultiplier), Image.SCALE_DEFAULT);


            }
            if (e.getKeyCode() == 39){ // select track
                GamePanel.chosenTrackIndex += 1;
                GamePanel.chosenTrackIndex  = GamePanel.chosenTrackIndex % GamePanel.trackList.size();
                // update image
                trackimage = Toolkit.getDefaultToolkit().createImage(GamePanel.trackList.get(GamePanel.chosenTrackIndex) + ".png").getScaledInstance((int) (600 * GamePanel.scaleMultiplier), (int) (600 * GamePanel.scaleMultiplier), Image.SCALE_DEFAULT);

            }
        }
        else if(GamePanel.gameRunning == 2){ // similarly, selects car.
            if (e.getKeyCode() == 37){
                org.w3c.dom.Element car;
                GamePanel.chosencarID -= 1;
                GamePanel.chosencarID += GamePanel.carList.getLength();
                GamePanel.chosencarID  = GamePanel.chosencarID % GamePanel.carList.getLength();
                car = (org.w3c.dom.Element) GamePanel.carList.item(GamePanel.chosencarID);
                carImage = Toolkit.getDefaultToolkit().createImage(car.getAttribute("id") + "icon.jpg").getScaledInstance((int) (1000 * GamePanel.scaleMultiplier), (int) (600 * GamePanel.scaleMultiplier), Image.SCALE_DEFAULT);
            }
            if (e.getKeyCode() == 39){
                org.w3c.dom.Element car;
                GamePanel.chosencarID += 1;
                GamePanel.chosencarID = GamePanel.chosencarID % GamePanel.carList.getLength();
                car = (org.w3c.dom.Element) GamePanel.carList.item(GamePanel.chosencarID);
                carImage = Toolkit.getDefaultToolkit().createImage(car.getAttribute("id") + "icon.jpg").getScaledInstance((int) (1000 * GamePanel.scaleMultiplier), (int) (600 * GamePanel.scaleMultiplier), Image.SCALE_DEFAULT);

            }
        }

    }
    public void draw(Graphics g) {
        if(GamePanel.gameRunning == 0){// starting splash screen
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(GamePanel.splashScreenBG, 0,0,null);
            g2d.setFont(new Font("Arial", Font.PLAIN, (int) (30*GamePanel.scaleMultiplier)));// sets text font
            // displays information text
            g2d.drawString("Press space to play", (int) (300* GamePanel.scaleMultiplier), (int) (1090* GamePanel.scaleMultiplier));
            g2d.drawString("W: gas. S:brake; A,D: Steering. R: toggle reverse gear", (int) (250* GamePanel.scaleMultiplier), (int) (1180* GamePanel.scaleMultiplier));
        }
        else if(GamePanel.gameRunning == 1){// race track picking
            g.setColor(Color.white);
            g.fillRect(0,0,(int) (1200 * GamePanel.scaleMultiplier), (int) (1200 * GamePanel.scaleMultiplier));

            g.setColor(Color.black);
            g.drawImage(trackimage, (int) (300 * GamePanel.scaleMultiplier),(int) (300 * GamePanel.scaleMultiplier), null); // draws the map
            g.setFont(new Font("Arial", Font.PLAIN, (int) (30*GamePanel.scaleMultiplier)));// sets text font
            // displays information text
            g.drawString("RaceTrack: " + GamePanel.trackList.get(GamePanel.chosenTrackIndex) , (int) (300* GamePanel.scaleMultiplier), (int) (250* GamePanel.scaleMultiplier));
            g.drawString("Use left / right arrow keys to change. Press space to select and continue." , (int) (50* GamePanel.scaleMultiplier), (int) (950* GamePanel.scaleMultiplier));

        }
        else if(GamePanel.gameRunning == 2){ //car picking
            Element car = (org.w3c.dom.Element) GamePanel.carList.item(GamePanel.chosencarID);
            String[] carDesc = car.getElementsByTagName("description").item(0).getTextContent().split("/");
            g.setColor(Color.white);
            g.fillRect(0,0,(int) (1200 * GamePanel.scaleMultiplier), (int) (1200 * GamePanel.scaleMultiplier));
            g.setColor(Color.black);
            g.drawImage(carImage, (int) (100 * GamePanel.scaleMultiplier),(int) (100 * GamePanel.scaleMultiplier), null); // draws the car.
            g.setFont(new Font("Arial", Font.PLAIN, (int) (30*GamePanel.scaleMultiplier)));// sets text font
            // displays information text

            g.drawString("Car: " + car.getElementsByTagName("carName").item(0).getTextContent() , (int) (300* GamePanel.scaleMultiplier), (int) (800* GamePanel.scaleMultiplier));
            g.drawString("Use left / right arrow keys to change. Press space to select and continue." , (int) (50* GamePanel.scaleMultiplier), (int) (1150* GamePanel.scaleMultiplier));

            g.setFont(new Font("Arial", Font.PLAIN, (int) (30*GamePanel.scaleMultiplier)));// sets text font

            g.drawString(carDesc[0] , (int) (50* GamePanel.scaleMultiplier), (int) (900* GamePanel.scaleMultiplier));
            g.drawString(carDesc[1] , (int) (50* GamePanel.scaleMultiplier), (int) (980* GamePanel.scaleMultiplier));

        }
        else if(GamePanel.gameRunning == 5){ // end condition screen.
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(GamePanel.splashScreenBG, 0,0,null);
            g2d.setFont(new Font("Arial", Font.PLAIN, (int) (30*GamePanel.scaleMultiplier)));// sets text font
            // displays information text
            g2d.drawString(GamePanel.endScreenMessage, (int) (300* GamePanel.scaleMultiplier), (int) (1090* GamePanel.scaleMultiplier));
            g2d.drawString("Press space to play again. ", (int) (300* GamePanel.scaleMultiplier), (int) (1130* GamePanel.scaleMultiplier));


        }
    }
}

