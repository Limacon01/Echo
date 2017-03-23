package echo.Computational;

import echo.GUI;
import echo.Sounds;

import javax.sound.sampled.AudioInputStream;
import javax.swing.*;
import java.io.File;
import java.util.List;

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
    private static final String  KEY1       = "1ac04cd4347b49a2b89052edf1a45ef0";
    private static final float   CONFIDENCE_THREASHOLD =  0.4f;

    private GUI       updatedGUI;
    private Detective sherlock;
    private TextToSpeech t2s;
    private WolframQuery wq = new WolframQuery();

    public BackgroundWorker(GUI gui){
        this.updatedGUI = gui;

        t2s = new TextToSpeech();
        wq = new WolframQuery();

        System.out.println("backgroundworker created");
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
            System.out.println("attempting to play error message");
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

        checkForInterrupt();
        publish("FinishedAnswering");
        return null;
    }

    void checkForInterrupt() throws InterruptedException {
        if (this.isCancelled()) {
            System.out.println("BG worker stopped");

            throw new InterruptedException();
        }
    }

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

    private String processSpeechToText(){
        final String token  = SpeechToText.renewAccessToken( KEY1 );
        final byte[] speech = SpeechToText.readData( INPUT );
        final String jsonText   = SpeechToText.recognizeSpeech( token, speech );
        return parseJsonFromMicrosoft(jsonText);
    }

    /**
     * This function takes a recorded question string in json format
     * and returns the processed result string as long as Microsoft's
     * cognitive service's confidence threshold is above a defined value
     * @param json  input string
     * @return
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
            if(foundThreshold > CONFIDENCE_THREASHOLD){
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
