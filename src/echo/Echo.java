package echo;

import echo.Computational.*;
import javax.sound.sampled.AudioInputStream;
import java.io.File;

/**
 * Main controller for the Echo device
 */
public class Echo implements SoundDetectedListener, StartListeningListener {
    private static final String  FILENAME   = "query.wav";
    private static final String  INPUT      = "./query.wav";
    private static final String  KEY1       = "1ac04cd4347b49a2b89052edf1a45ef0";
    private static final String  ERROR_SEND = "I'm sorry, I could not process the voice command";
    private static final String  ERROR_RETRY= "I'm sorry, I could not understand that - please repeat";


    private GUI gui;
    private Detective detective;
    private TextToSpeech t2s = new TextToSpeech();
    private WolframQuery wq = new WolframQuery();

    Echo(){
        gui = new GUI();
        gui.addListener(this);

        detective = new Detective();
        detective.addListener(this);;
    }

    String processSpeechToText(){
        final String token  = SpeechToText.renewAccessToken( KEY1 );
        final byte[] speech = SpeechToText.readData( INPUT );
        final String jsonText   = SpeechToText.recognizeSpeech( token, speech );

        return parseJsonFromMicrosoft(jsonText);
    }

    String parseJsonFromMicrosoft(String json){
        String parsedString = "";

        //If status success
        //find name:""
        //Else return ""

        System.out.println(json);
        return parsedString;
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

        //Currently returning as json
        String toSendToWolfram = processSpeechToText();

        toSendToWolfram = "Who invented the steam engine?";
        File outputFile;

        if(toSendToWolfram.equals("")){
            //Create appropriate file
            outputFile =  t2s.outputSpeechToFile(ERROR_SEND);
            System.out.println("attempting to play error message");
        }else {
            //Send to wolfram
            String result = wq.processQuestion(toSendToWolfram);

            //Create appropriate file
            outputFile = t2s.outputSpeechToFile(result);
        }
        //Play text-to-speech

        System.out.println();
        Sounds s = new Sounds(outputFile);
        s.run();
    }
}

