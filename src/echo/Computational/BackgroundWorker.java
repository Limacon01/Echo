package echo.Computational;

import echo.GUI;
import echo.Sounds;

import javax.sound.sampled.AudioInputStream;
import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * @version 2.0
 * This handles the background processing for the echo.
 * It runs in a background thread so that it does not lock up the GUI.
 * Handles:
 * Detecting sound
 * Recording sound
 * Processing speech to text
 * Processing the resultant JSON
 * Outputting the speech to a file
 * Playing the file
 */

public class BackgroundWorker extends SwingWorker<Integer, String> {
    private static final String  ERROR_SEND = "I'm sorry, I could not process the voice command";
    private static final String  FILENAME   = "query.wav";
    private static final String  INPUT      = "./query.wav";
    private static final String  KEY1       = "1ac04cd4347b49a2b89052edf1a45ef0";
    private static final float CONFIDENCE_THRESHOLD =  0.4f;

    private GUI       updatedGUI;
    private Detective sherlock;
    private TextToSpeech t2s;
    private WolframQuery wq = new WolframQuery();

    /**
     * Get access to the GUI, initialise a Detective, text-to-speech
     * and wolfram query instance.
     *
     * @param gui           The GUI attached to the background
     *                      worker
     */
    public BackgroundWorker(GUI gui){
        this.updatedGUI = gui;
        sherlock = new Detective();
        t2s = new TextToSpeech();
        wq = new WolframQuery();
    }

    /**
     * This method handles all the computation work that is done in
     * the background of the echo.
     *
     * @throws Exception    Throws InterruptedException if gui calls
     *                      BackgroundWorker.Cancel()
     */
    @Override
    protected Integer doInBackground() throws Exception {

        //Wait for detective to detect sound
        Thread t1 = new Thread(sherlock, "t1");
        t1.start();

        //Detect when detective is back
        t1.join();

        checkForInterrupt();
        publish("Sound detected");

        //Record the sound
        AudioInputStream ais = RecordSound.setupStream();
        RecordSound.recordSound(FILENAME, RecordSound.readStream(ais));
        RecordSound.closeDataLine();

        //Update the GUI to answer mode
        checkForInterrupt();
        publish("AnswerMode");

        //ProcessSpeechToText
        String toSendToWolfram = processSpeechToText();
        File outputFile;
        if (toSendToWolfram.equals("")) {
            //Create appropriate file
            outputFile = t2s.outputSpeechToFile(ERROR_SEND);
        } else {
            //Send to wolfram
            String result = wq.processQuestion(toSendToWolfram);

            //Create appropriate file
            outputFile = t2s.outputSpeechToFile(result);
        }

        //Play text-to-speech
        Sounds s = new Sounds(outputFile);
        double length = s.getLengthOfFile(outputFile);

        checkForInterrupt();
        Thread t2 = new Thread(new Sounds(outputFile));
        t2.start();
        t2.join();

        Thread.sleep((int)length*1000);
        checkForInterrupt();
        publish("FinishedAnswering");
        return null;
    }

    /**
     * @throws InterruptedException This method throws an exception
     *                              if BackgroundWorker.cancel() is called
     */
    public void checkForInterrupt() throws InterruptedException {
        if (this.isCancelled()) {
            System.out.println("Stopping background worker");
            throw new InterruptedException();
        }
    }

    /**
     * A String list containing strings from
     * publish("STRING") in doInBackground();
     * @param chunks        String list of strings that get published
     */
    @Override
    protected void process(final List<String> chunks){
        //Updates the GUI
        for(final String string : chunks){
            if(string.equals("AnswerMode")){
                updatedGUI.setAnswer();
            }else if(string.equals("FinishedAnswering")){
                updatedGUI.setListen();
            }
        }
    }

    /**
     * Converts speech file to a JSON formatted string and then
     * extracts the plaintext question
     * @return              A plaintext question
     */
    private String processSpeechToText(){
        final String token  = SpeechToText.renewAccessToken( KEY1 );
        final byte[] speech = SpeechToText.readData( INPUT );
        final String jsonText   = SpeechToText.recognizeSpeech( token, speech );
        return parseJsonFromMicrosoft(jsonText);
    }

    /**
     * This function takes a recorded question string in json format
     * and returns the processed result string as long as Microsoft's
     * cognitive service's confidence threshold is above a defined
     * value
     * @param json          JSON formatted String
     * @return              A Plaintext string
     */
    public static String parseJsonFromMicrosoft(String json){
        String succStr = "\"status\":\"success\"";
        int status = json.indexOf(succStr);

        //If we get a success
        if(status != -1){
            // and confidence is above 0.4
            String confidenceString = "\"confidence\":\"";
            int confidenceThreshold = json.indexOf(confidenceString) + confidenceString.length();
            float foundThreshold = Float.parseFloat(json.substring(confidenceThreshold, confidenceThreshold+5));
            if(foundThreshold > CONFIDENCE_THRESHOLD){
                //We're sure this the question string
                String startOfQuestion = "\"name\":\"";
                String endOfQuestion = "\",\"lexical\":";

                int questionIndex = json.indexOf(startOfQuestion) + startOfQuestion.length();
                int endQuestionIn = json.indexOf(endOfQuestion);
                String parsedString = json.substring(questionIndex, endQuestionIn);
                System.out.println(parsedString);
                return parsedString;
            }
        }
        return "";
    }

}
