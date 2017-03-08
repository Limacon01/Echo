package echo;
/*
* What does this do?
*   Initiates GUI for an Echo in Off mode
*   Toggle button for power
*   When power button is turned on, lights will glow Cyan to show listening mode has been entere
*/

/*
* To do:
*   Get JFrame functioning properly on multiple resolutions
*   Add light mode for answer state
*   Integrate listeners for microphone use for answer mode
*   Make 3D model of Amazon Echo for nicer viewing
*   Make GIFs for lights
*   Implement testing
*/

import echo.Computational.StartListeningListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {
    private static final int startupCoolDown = 0;
    private static final int shutdownCoolDown = 0;
    private boolean isDisabled = false;

    private static String status = "OFF"; //Global variable each operating mode
    private static String resdir = "./src/echo/Resources/Images/";
    private final PowerButton   power = new PowerButton();
    private final Light         light = new Light();
    private Sounds sound;

    private List<StartListeningListener> startListeningListeners = new ArrayList<>();

    public void addListener(StartListeningListener sdl) {
        System.out.println("adding detection listener");
        startListeningListeners.add(sdl);
    }

    /* On/Off button */
    private class PowerButton extends JToggleButton {
        PowerButton() {
            setIcon(new ImageIcon(resdir + "powerOFF.png"));
            setSelectedIcon(new ImageIcon(resdir + "powerON.png"));
            setBorder(null);
            setContentAreaFilled(false);
        }
    }

    /* Lights */
    private class Light extends JButton {
        Light() {
            setIcon(new ImageIcon(resdir + "light" + status + ".png"));
            setBorder(null);
            setContentAreaFilled(false);
        }
    }

    public GUI() {
        setTitle("Echo");
        setContentPane(new JLabel(new ImageIcon(resdir + "background.png")));
        setLayout(null);

        power.setBounds(900, 705,  115,   105);     add(power);
        light.setBounds(  0,   0, 1920,  1080);     add(light);

        /* Configures the screen */
        this.setSize(1600, 900);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();


        /*
        *   Illuminate transitions from:
        *       OFF -> Listening mode
        *       Listening mode -> OFF
        */
        power.addActionListener(ev -> {
            if (!isDisabled) {
                isDisabled = true;
                switch (status) {
                /* Turning echo from off to on */
                    case "OFF":
                        status = "LISTEN";
                        light.setIcon(new ImageIcon(resdir + "light" + status + ".png"));

                        sound = new Sounds("ON");
                        sound.run();

                        //Do echo stuff
                        for (StartListeningListener sll: startListeningListeners) {
                            sll.startListening();
                        }

                        try {
                            Thread.sleep(startupCoolDown);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;

                /* Turning echo from on to off */
                    case "LISTEN":
                        status = "OFF";
                        light.setIcon(new ImageIcon(resdir + "light" + status + ".png"));

                        sound = new Sounds("OFF");
                        sound.run();

                        //Do echo stuff

                        try {
                            Thread.sleep(startupCoolDown);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                //Finished turning on or off
                System.out.println("Status after clicking:" + status);
                isDisabled = false;
            }
        });
    }
}
