package echo;

import echo.Computational.BackgroundWorker;

import javax.swing.*;
import java.net.URL;

/**
 * Initiates GUI for an Echo in Off mode
 * Toggle button for power
 * When power button is turned on, lights will glow Cyan to show listening mode has been entered
 * (not fully functional at the moment)
 */
public class GUI extends JFrame {
    private String status = "OFF";
    private boolean firstTime = true;

    private final PowerButton   power;
    private final Light         light;

    private Sounds sound;
    private BackgroundWorker backgroundWorker;

    public GUI() {
        power = new PowerButton();
        addPowerListener();
        light = new Light();
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
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
                    setFirstTime();
                    //Updates the gui... plays the sounds...
                    setListen();
                    break;
                /* Turning echo from on to off */
                case "LISTEN":
                    System.out.println("Attempted to interupt background worker");
                    backgroundWorker.cancel(true);

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
        sound = new Sounds("OFF");
        sound.run();
    }

    public void setListen(){
        //This updates the gui...
        setStatus("LISTEN");
        if(firstTime) {
            //Runs threaded sound
            sound = new Sounds("ON");
            sound.run();
            int secondstoWait = 1;

            try { Thread.sleep(secondstoWait * 1000);}
            catch (Exception e) {e.printStackTrace();}

            firstTime = false;
        }

        if(backgroundWorker != null) {
            backgroundWorker.cancel(true);
        }
        backgroundWorker = new BackgroundWorker(GUI.this);
        backgroundWorker.execute();
    }

    public void setAnswer(){
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

    public void setFirstTime(){
        this.firstTime = true;
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
}
