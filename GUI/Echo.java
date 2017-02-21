import java.awt.Color;
import java.awt.TextField;
import java.awt.event.*;
import javax.swing.*;

public class Echo extends JFrame {

    private static String status = "OFF"; // LISTEN (cyan) or ANSWER (blue)

    final PowerButton   power = new PowerButton();
    final Light         light = new Light(status);


    private class PowerButton extends JToggleButton {
        PowerButton() {
            setIcon(new ImageIcon("powerOFF.png"));
            setSelectedIcon(new ImageIcon("powerON.png"));
            setBorder(null);
            setContentAreaFilled(false);
                    }

    }

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
        light.setBounds(44, 50, 257, 100); add(light);

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
        frame.setLocationRelativeTo(null);
        frame.setSize(350, 900);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
