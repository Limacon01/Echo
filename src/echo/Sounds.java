package echo;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The Sounds class selects the appropriate wav file to play
 * when the Echo is turned on or off, or when there is an
 * error in picking up audio.
 *
 * @author Alex and Mark
 * @version 1.0
 */
public class Sounds implements Runnable {
    URL soundRes;
    File desiredFile;
    /**
     * @param status    the status of the Echo which determines which sound file is to be used
     */
    public Sounds(String status){
        switch (status) {
            case "ON":
                soundRes = this.getClass().getResource("/echo/Resources/Sounds/onSound.wav");
                break;
            case "OFF":
                soundRes = this.getClass().getResource("/echo/Resources/Sounds/offSound.wav");
                break;
            case "ANSWER":
                soundRes = this.getClass().getResource("speechOutput.wav");
                break;
            default:
                soundRes = this.getClass().getResource("/echo/Resources/Sounds/errorMessage.wav");
                break;
        }
    }

    public Sounds(File file){
        desiredFile = file;
    }

    /**
     * This run method will play the selected wav file
     */

    //TODO: Change this 110%
    @Override
    public void run() {
        try {
            if(soundRes != null) {
                System.out.println("Sound running");
                Clip clip = AudioSystem.getClip();
                AudioInputStream as = AudioSystem.getAudioInputStream(soundRes);
                clip.open(as);
                clip.start();
                soundRes = null;
            }else if(desiredFile != null){
                System.out.println("Playing file");
                Clip clip = AudioSystem.getClip();
                AudioInputStream as = AudioSystem.getAudioInputStream(desiredFile);
                clip.open(as);
                clip.start();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
