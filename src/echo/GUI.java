package echo;

import echo.Computational.StartListeningListener;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    private final PowerButton   power = new PowerButton();
    private final Light         light = new Light();

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

    public GUI() {
        initGUI();
        addPowerListener();
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

    void setOff(){
        power.setEnabled(true);
        setStatus("OFF");
        sound = new Sounds("OFF", this);
        sound.run();
    }

    void setListen(){
        setStatus("LISTEN");
        sound = new Sounds("ON", this);
        sound.run();

        //Once sound has finished playing, and the gui has been updated... startListening for sound
        SwingUtilities.invokeLater(() -> startListeningListeners.forEach(StartListeningListener::startListening));
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
