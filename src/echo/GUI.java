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

import javax.swing.*;

public class GUI extends JFrame {

    private static String status = "OFF"; //Global variable each operating mode
    private static String resdir = "./src/echo/Resources/Images/";
    final PowerButton   power = new PowerButton();
    final Light         light = new Light(status);

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
        Light(String mode) {
            setIcon(new ImageIcon(resdir + "light" + status + ".png"));
            setBorder(null);
            setContentAreaFilled(false);
        }
    }

    public GUI() {
        setTitle("Echo");
        setContentPane(new JLabel(new ImageIcon(resdir + "background.png")));
        setLayout(null);

        power.setBounds(147, 623, 47, 50); add(power);
        light.setBounds(44, 50, 257, 100); add(light);

        /*
        *   Illuminate transitions from:
        *       OFF -> Listening mode
        *       Listening mode -> OFF
        */
        power.addActionListener(ev -> {
            switch (status) {
                case "OFF":
                    status = "LISTEN";  break;
                case "LISTEN":
                    status = "OFF";     break;
            }
            light.setIcon(new ImageIcon(resdir + "light" + status + ".png"));
        });

        /* Configures the screen */
        this.setSize(350, 900);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }
}
