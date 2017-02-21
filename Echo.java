import java.awt.Color;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Echo extends JFrame {

    private static String status = "OFF";
    private static String mode = "LISTEN"; // LISTEN (cyan) or ANSWER (blue)

    final PowerButton power = new PowerButton();
    final Light light = new Light(mode);



    private class PowerButton extends JToggleButton {
        PowerButton() {
            setIcon(new ImageIcon("powerOFF.png"));
            setSelectedIcon(new ImageIcon("powerON.png"));
            setBorder(null);
            setContentAreaFilled(false);
            if (isSelected()) {
                status = "ON";
                updateUI();
            }else status = "OFF";
            validate();
            repaint();
        }
    }

    private class Light extends JButton {
        Light(String mode) {
            if (power.isSelected()){
                        setIcon(new ImageIcon("light" + mode + ".png"));
                    }else{
                        setIcon(new ImageIcon("light" + status + ".png"));;
            setBorder(null);
            setContentAreaFilled(false);
        }
    }}

    public Echo() {
        setTitle("Echo");
        setContentPane(new JLabel(new ImageIcon("background.png")));
        setLayout(null);


        power.setBounds(117, 573, 47, 50); add(power);
        light.setBounds(12, 0, 257, 100);  add(light);

    }

    public static void main(String[] argv) {
        JFrame frame = new Echo();
        frame.setLocationRelativeTo(null);
        frame.setSize(287, 780); /* title bar! */
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
