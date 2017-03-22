package echo.Computational;

import echo.Echo;
import echo.GUI;
import echo.Sounds;

import javax.sound.sampled.AudioInputStream;
import javax.swing.*;
import java.io.File;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by Mark on 22/03/2017.
 */

//First: int when finished working...
//Second: status of work
public class BackgroundWorker extends SwingWorker<Integer, String> {
    private static final String  ERROR_SEND = "I'm sorry, I could not process the voice command";
    private static final String  ERROR_RETRY= "I'm sorry, I could not understand that - please repeat";
    private static final String  FILENAME   = "query.wav";
    private static final String  INPUT      = "./query.wav";
    private static final String  KEY1       = "1ac04cd4347b49a2b89052edf1a45ef0";;

    private GUI       updatedGUI;
    private Detective sherlock;
    private TextToSpeech t2s;
    private WolframQuery wq = new WolframQuery();

    public BackgroundWorker(GUI gui){
        this.updatedGUI = gui;

        t2s = new TextToSpeech();
        wq = new WolframQuery();
    }

    @Override
    protected Integer doInBackground() throws Exception {
        //Create detective
        sherlock = new Detective();

        //Wait for detective to detect sound
        Thread t1 = new Thread(sherlock, "t1");
        t1.start();

        //Detect when detective is back
        t1.join();
        publish("Sound detected");
        failIfInterrupted();

        //Record the sound
        AudioInputStream ais = RecordSound.setupStream();
        RecordSound.recordSound(FILENAME, RecordSound.readStream(ais));
        RecordSound.closeDataLine();
        publish("Sound Successfully recorded");
        failIfInterrupted();

        publish("AnswerMode");

        //ProcessSpeechToText
        String toSendToWolfram = processSpeechToText();
        File outputFile;
        if (toSendToWolfram.equals("")) {
            //Create appropriate file
            outputFile = t2s.outputSpeechToFile(ERROR_SEND);
            System.out.println("attempting to play error message");
        } else {
            //Send to wolfram
            String result = wq.processQuestion(toSendToWolfram);

            //Create appropriate file
            outputFile = t2s.outputSpeechToFile(result);
        }

        //Play text-to-speech
        Sounds s = new Sounds(outputFile);
        double lengthOfFileSeconds = s.getLengthOfFile(outputFile) + 2;
        s.run();
        return null;
    }

    private static void failIfInterrupted() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Interrupted.... shutting down");
        }
    }

    @Override
    protected void process(final List<String> chunks){
        //Update the gui?
        for(final String string : chunks){
            System.out.println(string + " \n");
        }
    }

    public void cancel(){
        this.cancel(true);
        System.out.println("Quit listening mode - byebye!");
    }

    String processSpeechToText(){
        final String token  = SpeechToText.renewAccessToken( KEY1 );
        final byte[] speech = SpeechToText.readData( INPUT );
        final String jsonText   = SpeechToText.recognizeSpeech( token, speech );
        return Echo.parseJsonFromMicrosoft(jsonText);
    }
}
