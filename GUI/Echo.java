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

import java.awt.event.*;
import javax.swing.*;

public class Echo extends JFrame {

    private static String status = "OFF"; //Global variable each operating mode

    final PowerButton   power = new PowerButton();
    final Light         light = new Light(status);

    /* On/Off button */
    private class PowerButton extends JToggleButton {
        PowerButton() {
            setIcon(new ImageIcon("powerOFF.png"));
            setSelectedIcon(new ImageIcon("powerON.png"));
            setBorder(null);
            setContentAreaFilled(false);
        }
    }

    /* Lights */
    private class Light extends JButton {
        Light(String mode) {
            setIcon(new ImageIcon("light" + status + ".png"));
            setBorder(null);
            setContentAreaFilled(false);
        }
    }

    public Echo() {
        setTitle("Echo");
        setContentPane(new JLabel(new ImageIcon("background.png")));
        setLayout(null);

        power.setBounds(147, 623, 47, 50); add(power);
        light.setBounds(46, 57, 257, 100); add(light); //Currently works on resolution @ Blue Room

        /*
        *   Illuminate transitions from:
        *       OFF -> Listening mode
        *       Listening mode -> OFF
        */
        power.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                switch (status) {
                    case "OFF":
                        status = "LISTEN";  break;
                    case "LISTEN":
                        status = "OFF";     break;
                }
                light.setIcon(new ImageIcon("light" + status + ".png"));
            }
        });
    }

    public static void main(String[] argv) {
        JFrame frame = new Echo();
        frame.setSize(350, 900);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
