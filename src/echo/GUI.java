package echo;

import echo.Computational.BackgroundWorker;

import javax.swing.*;
import java.net.URL;

/**
 * @version 3.0
 *
 * Spawns and holds control methods for all things GUI
 */
public class GUI extends JFrame {
    // Public for testing purposes:
    // (these fields are not manipulated directly by any other classes)
    public String status = "OFF";
    public boolean firstTime = true;

    public final PowerButton   power;
    private final Light         light;

    private Sounds sound;
    private BackgroundWorker backgroundWorker;

    /**
     * Spawn JButtons and call method to spawn and configure JFrame
     */
    public GUI() {
        power = new PowerButton();
        addPowerListener();
        light = new Light();
        initGUI();
    }

    /**
     * Spawn and configure JFrame
     */
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

    /**
     * Action listener for power button
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

    /**
     * Change light to match current mode of Echo
     * @param status        Status of Echo: OFF, LISTEN or ANSWER
     */
    void updateLight(String status){
        URL lightSTATUS = this.getClass().getResource("/echo/Resources/Images/light" + status + ".png");
        light.setIcon(new ImageIcon(lightSTATUS));
        light.revalidate();
    }

    /**
     * Change power button lighting to match current mode of Echo
     * @param status        Status of Echo: OFF, LISTEN or ANSWER
     */
    void updatePowerButton(String status){
        URL powerONLoc = this.getClass().getResource("/echo/Resources/Images/power" + status + ".png");
        power.setIcon(new ImageIcon(powerONLoc));
        power.revalidate();
    }

    /**
     * Updates GUI to OFF mode and runs according sounds
     */
    public void setOff(){
        power.setEnabled(true);
        setStatus("OFF");
        sound = new Sounds("OFF");
        sound.run();
    }

    /**
     * Updates GUI to Listen mode and runs according sounds
     */
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

    /**
     * Updates GUI to Answer mode
     */
    public void setAnswer(){
        setStatus("ANSWER");
    }

    /**
     * Updates lights and power button to argument status
     * @param status        Status of Echo: OFF, LISTEN or ANSWER
     */
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

    /**
     * Only run Listen sound on first run
     */
    public void setFirstTime(){
        this.firstTime = true;
    }

    /**
     * JButton for power button
     * See method updatePowerButton to update icon
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
     * JButton to act as a graphic for light
     * See method updateLight to update icon
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
