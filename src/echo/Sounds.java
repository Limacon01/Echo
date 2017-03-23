package echo;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * The Sounds class selects the appropriate wav file to play
 * when the Echo is turned on or off, or when there is an
 * error in picking up audio.
 *
 * @version 1.0
 */
public class Sounds implements Runnable {
    public URL     soundRes;
    public File    desiredFile;
    public AudioInputStream as;

    /**
     * @param file  The input file that will be run in the .run() method
     */
    public Sounds(File file){
        desiredFile = file;
    }

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
            default:
                soundRes = this.getClass().getResource("/echo/Resources/Sounds/errorMessage.wav");
                break;
        }
    }

    /**
     * @param file
     * @return
     */
    public static double getLengthOfFile(File file){
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
        }catch(Exception e){
            e.printStackTrace();
        }
        AudioFormat format = audioInputStream.getFormat();
        long frames = audioInputStream.getFrameLength();
        double duration = (frames+0.0) / format.getFrameRate();
        return duration;   //in seconds
    }

    /**
     * This method assigns a file to an AudioInputStream
     */
    public void assignAudioInputStream() throws IOException, UnsupportedAudioFileException {
        if(soundRes != null) {
            as = AudioSystem.getAudioInputStream(soundRes);
            soundRes = null;
        }else if(desiredFile != null){
            as = AudioSystem.getAudioInputStream(desiredFile);
        }else{
            System.out.println("Could not find input file");
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
            assignAudioInputStream();
            clip.open(as);
            clip.start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
