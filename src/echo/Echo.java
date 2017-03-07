package echo;

import echo.Computational.Detective;

import javax.swing.*;

public class Echo{
    //Setup gui -- where sound is integrated.

    Echo(){
        JFrame gui = new GUI();
        // Detective creates an *insert helpfully named event* once a sound is detected
        Detective d = new Detective();
        d.run();

        /*

         */
        //Computational c = new Computational();
    }

}
