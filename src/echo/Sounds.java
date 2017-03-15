package echo;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.MalformedURLException;
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
    URL soundRes;
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
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                soundRes = this.getClass().getResource("./speechOutput.wav");
                break;
            /*default:
                soundRes = this.getClass().getResource("/echo/Resources/Sounds/errorMessage.wav");
                break;*/
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
            AudioInputStream as = AudioSystem.getAudioInputStream(soundRes);
            clip.open(as);
            clip.start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
