package echo;
import echo.Computational.*;

import javax.sound.sampled.AudioInputStream;
import javax.swing.*;

public class Echo implements SoundDetectedListener{
    //Setup gui -- where sound is integrated.
    private static final String  FILENAME   = "query.wav";
    private static final String  INPUT      = "./query.wav";
    private static final String  KEY1       = "1ac04cd4347b49a2b89052edf1a45ef0";

    Echo(){
        JFrame gui = new GUI();

        // Detective creates an *insert helpfully named event* once a sound is detected
        Detective d = new Detective();
        d.addListener(this);
        d.run();
        //Send to wolfram

        //Text to speech
    }

    @Override
    public void soundDetected() {
        //Record sound for 5s
        System.out.println("Sound detected");
        AudioInputStream ais = RecordSound.setupStream();
        RecordSound.recordSound(FILENAME, RecordSound.readStream(ais));

        /*
         * Convert speech to text.
        */
        processSpeechToText();
    }

    String processSpeechToText(){
        final String token  = SpeechToText.renewAccessToken( KEY1 );
        final byte[] speech = SpeechToText.readData( INPUT );
        final String jsonText   = SpeechToText.recognizeSpeech( token, speech );

        //This is JSON text recieved back from microsoft
        System.out.println(jsonText);
        return jsonText;
    }
}
