package echo;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * Sounds file
 */

class Sounds implements Runnable{
    private static String resDir = "./src/echo/Resources/Sounds/";
    private File desiredFile;
    private boolean playing;

    Sounds(String status){
        switch (status) {
            case "ON":
                desiredFile = new File( resDir + "onSound.wav");
                break;
            case "OFF":
                desiredFile = new File( resDir + "offSound.wav");
                break;
            default:
                desiredFile = new File( resDir + "errorMessage. wav");
                break;
        }
    }

    @Override
    public void run() {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream as = AudioSystem.getAudioInputStream(desiredFile);
            clip.open(as);
            clip.start();
            //clip.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
