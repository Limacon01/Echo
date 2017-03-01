import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * Created by Alex on 22/02/2017.
 */

public class PlaySound {

    public static void main(String[] args) throws InterruptedException {
        File opening = new File( "starwars.wav");
        File closing = new File( "westlife.wav");
        File error = new File( "errorMessage.wav");

        PlaySound(opening);
        Thread.sleep(2000);
        PlaySound(error);
        Thread.sleep(2000);
        PlaySound(closing);

    }

    static void PlaySound(File Sound){
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(Sound));
            clip.start();

            Thread.sleep(clip.getMicrosecondLength()/1000);
        } catch(Exception e){
            //catch
        }
    }
}
