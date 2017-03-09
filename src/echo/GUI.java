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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {
    private static final int startupCoolDown = 0;
    private static final int shutdownCoolDown = 0;
    private boolean isDisabled = false;

    private static String status = "OFF"; //Global variable each operating mode
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
            URL powerOFFLoc = this.getClass().getResource("/echo/Resources/Images/powerOFF.png");
            URL powerONLoc = this.getClass().getResource("/echo/Resources/Images/powerON.png");

            setIcon(new ImageIcon(powerOFFLoc));
            setSelectedIcon(new ImageIcon(powerONLoc));
            setBorder(null);
            setContentAreaFilled(false);
        }
    }

    /* Lights */
    private class Light extends JButton {
        Light() {
            URL light = this.getClass().getResource("/echo/Resources/Images/" + "light" + status + ".png");
            setIcon(new ImageIcon(light));
            setBorder(null);
            setContentAreaFilled(false);
        }
    }

    public GUI() {
        setTitle("Echo");
        URL background = this.getClass().getResource("/echo/Resources/Images/background.png");
        setContentPane(new JLabel(new ImageIcon(background)));
        setLayout(null);

        power.setBounds(150, 615,  115,   105);     add(power);
        light.setBounds(  0,   0, 420,  900);       add(light);

        /* Configures the screen */
        this.setSize(420, 900);
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
                        URL lightListenRes = this.getClass().getResource("/echo/Resources/Images/lightLISTEN.png");
                        light.setIcon(new ImageIcon(lightListenRes));
                        System.out.println("Tried to set icon");

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
                        URL lightOFFRes = this.getClass().getResource("/echo/Resources/Images/lightOFF.png");
                        light.setIcon(new ImageIcon(lightOFFRes));

                        sound = new Sounds("OFF");
                        sound.run();

                        try {
                            Thread.sleep(shutdownCoolDown);
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
