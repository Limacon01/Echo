package echo;

import echo.Computational.*;
import javax.sound.sampled.AudioInputStream;

/**
 * Main controller for the Echo device
 */
public class Echo implements SoundDetectedListener, StartListeningListener {
    private static final String  FILENAME   = "query.wav";
    private static final String  INPUT      = "./query.wav";
    private static final String  KEY1       = "1ac04cd4347b49a2b89052edf1a45ef0";

    private GUI gui;
    private Detective detective;
    private TextToSpeech t2s = new TextToSpeech();

    Echo(){
        gui = new GUI();
        gui.addListener(this);

        detective = new Detective();
        detective.addListener(this);

        //TODO: Text to speech
        t2s.outputSpeechToFile("Frankly, my dear I don't give a damn!");

        Sounds s = new Sounds("ANSWER");
        s.run();
    }

    /**
     * Once a device is ready to start listening
     * This starts detective, which listens for a sound
     * over a specific threshold
     */
    @Override
    public void startListening() {
        detective.run();
    }

    /**
     * Once a sound is detected, record sound for 5s
     * and process it
     */
    @Override
    public void soundDetected() {
        //Record sound for 5s
        System.out.println("Sound detected");
        AudioInputStream ais = RecordSound.setupStream();
        RecordSound.recordSound(FILENAME, RecordSound.readStream(ais));
        RecordSound.closeDataLine();
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
