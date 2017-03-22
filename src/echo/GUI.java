package echo;

import echo.Computational.StartListeningListener;
import echo.Computational.ThreadedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Initiates GUI for an Echo in Off mode
 * Toggle button for power
 * When power button is turned on, lights will glow Cyan to show listening mode has been entered
 * (not fully functional at the moment)
 * TODO:  add light mode for answer state
 * TODO:  integrate listeners for microphone use for answer mode
 * TODO:  make GIFs for lights
 */
public class GUI extends JFrame {

    private String status = "OFF";
    private final PowerButton   power;
    private final Light         light;
    public EchoApp EA;

    private Sounds sound;

    private List<StartListeningListener> startListeningListeners = new ArrayList<>();

    public void addListener(StartListeningListener sdl) {
        System.out.println("adding detection listener");
        startListeningListeners.add(sdl);
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

    /**
     * Single state button to act solely as a graphic
     */
    private class Light extends JButton {
        Light() {
            URL light = this.getClass().getResource("/echo/Resources/Images/" + "light" + status + ".png");
            setIcon(new ImageIcon(light));
            setBorder(null);
            setContentAreaFilled(false);
        }
    }

    public GUI(EchoApp EA) {
        power = new PowerButton();
        addPowerListener();
        light = new Light();
        this.EA = EA;

        initGUI();

    }

    private void initGUI(){
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
    }

    /*
       *   Illuminate transitions from:
       *       OFF -> Listening mode
       *       Listening mode -> OFF
       */
    private void addPowerListener(){
        power.addActionListener(ev -> {
            switch (status) {
                /* Turning echo from off to on */
                case "OFF":

                    //This is a kill switch
                    ThreadedButton tb = new ThreadedButton(this);
                    tb.run();

                    setListen();
                    break;
                /* Turning echo from on to off */
                case "LISTEN":
                    setOff();
                    break;
                /* Power button disabled while in answer mode */
                case "ANSWER":
                    break;
            }
            //Finished turning on or off
            System.out.println("Status after clicking:" + status);
        });
    }

    void updateLight(String status){
        URL lightSTATUS = this.getClass().getResource("/echo/Resources/Images/light" + status + ".png");
        light.setIcon(new ImageIcon(lightSTATUS));
        light.revalidate();
    }

    void updatePowerButton(String status){
        URL powerONLoc = this.getClass().getResource("/echo/Resources/Images/power" + status + ".png");
        power.setIcon(new ImageIcon(powerONLoc));
        power.revalidate();
    }

    boolean hasBeenClicked(){
        return false;
    }

    void setOff(){
        power.setEnabled(true);
        setStatus("OFF");
        sound = new Sounds("OFF", this);
        sound.run();

    }

    void setListen(){
        //This updates the gui...
        setStatus("LISTEN");

        //Runs threaded sound
        sound = new Sounds("ON", this);
        sound.run();

        //Pause the gui for 2 seconds
        int timeToWaitSeconds = 2;

        SwingUtilities.invokeLater(() -> {
            try{ sleep(timeToWaitSeconds*1000);}
            catch(Exception e){e.printStackTrace();}

            //Background thread checking for clicks?
            startListeningListeners.forEach(StartListeningListener::startListening);
        });
    }

    void setAnswer(){
        setStatus("ANSWER");
    }

    public void setStatus(String status){
        this.status = status;

        //Updates the light
        updateLight(status);

        //Updates the power button
        if (status.equals("LISTEN") || status.equals("ANSWER")) {
            updatePowerButton("ON");
        }else{
            updatePowerButton("OFF");
        }
        System.out.println("Status:" + status);
    }

    public String checkStatus(){
        return status;
    }
}
