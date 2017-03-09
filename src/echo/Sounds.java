package echo;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

/**
 * Sounds file
 */

class Sounds implements Runnable {
    //Need to set this to relative so it works
    URL soundRes;
    Sounds(String status){
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

    @Override
    public void run() {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream as = AudioSystem.getAudioInputStream(soundRes);
            clip.open(as);
            clip.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
