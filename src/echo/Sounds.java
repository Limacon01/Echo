package echo;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

/**
 * The Sounds class selects the appropriate wav file to play
 * when the Echo is turned on or off, or when there is an
 * error in picking up audio.
 *
 * @author Alex and Mark
 * @version 1.0
 */
public class Sounds implements Runnable {
    private URL     soundRes;
    private File    desiredFile;
    private GUI     gui;

    /**
     * @param file  The input file that will be run in the .run() method
     */
    public Sounds(File file){
        desiredFile = file;
    }

    /**
     * @param file The input file that will be run in the .run() method
     * @param gui  The gui that the Sounds object is run from
     */
    public Sounds(File file, GUI gui){
        this.gui = gui;
        desiredFile = file;
    }

    /**
     * @param status    the status of the Echo which determines which sound file is to be used
     */
    public Sounds(String status, GUI gui){
        this.gui = gui;
        switch (status) {
            case "ON":
                soundRes = this.getClass().getResource("/echo/Resources/Sounds/onSound.wav");
                break;
            case "OFF":
                soundRes = this.getClass().getResource("/echo/Resources/Sounds/offSound.wav");
                break;
            default:
                soundRes = this.getClass().getResource("/echo/Resources/Sounds/errorMessage.wav");
                break;
        }
    }

    /**
     * This run method will play the selected wav file
     */
    @Override
    public void run() {
        try {
            System.out.println("Sound running");
            Clip clip = AudioSystem.getClip();
            AudioInputStream as;
            if(soundRes != null) {
                as = AudioSystem.getAudioInputStream(soundRes);
                soundRes = null;
            }else if(desiredFile != null){
                as = AudioSystem.getAudioInputStream(desiredFile);
            }else{
                System.out.println("Could not find input file");
                return;
            }
            clip.open(as);
            clip.start();

            if(gui != null ){
                // disable gui
                gui.setSoundFinishedPlaying(false);

                while (clip.getMicrosecondLength() != clip.getMicrosecondPosition()) {
                }
                // re-enable gui
                gui.setSoundFinishedPlaying(true);
                System.out.println("Sound finished playing");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
