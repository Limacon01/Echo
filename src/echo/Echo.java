package echo;

import javax.swing.*;

public class Echo{
    //Setup gui -- where sound is integrated.

    Echo(){
        JFrame gui = new GUI();
        gui.setSize(350, 900);
        gui.setLocationRelativeTo(null);
        gui.setResizable(false);
        gui.setVisible(true);
    }

}
