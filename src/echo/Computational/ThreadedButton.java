package echo.Computational;

import echo.GUI;

import javax.swing.*;
import java.net.URL;

import static java.lang.Thread.sleep;

/**
 * Created by mark on 22/03/17.
 */
public class ThreadedButton extends JFrame implements Runnable {
    boolean running;
    PowerButton pb;
    GUI gui;

    public ThreadedButton(GUI gui){
        pb = new PowerButton();
        addPowerListener();
        this.gui = gui;
    }

    @Override
    public void run() {
        while(running){
            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRunning(){
        this.running = false;
    }

    /**
     * Two-state button with a different icon for each state
     * State change triggered by a mouse click upon button
     */
    private class PowerButton extends JButton {
        PowerButton() {
            //Looks of button
            URL powerOFFLoc = this.getClass().getResource("/echo/Resources/Images/powerOFF.png");
            setIcon(new ImageIcon(powerOFFLoc));
            setBorder(null);
            setContentAreaFilled(false);
        }
    }

    private void addPowerListener(){
        pb.addActionListener(ev -> {
            System.out.println("New button click");
            gui.EA.restartEcho();
        });
    }
}
