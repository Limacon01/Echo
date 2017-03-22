package echo;

import echo.Computational.*;

import javax.sound.sampled.AudioInputStream;
import javax.swing.*;
import java.io.File;

import static java.lang.Thread.sleep;

/**
 * Main controller for the Echo device
 */
public class Echo implements SoundDetectedListener, StartListeningListener {
    private static final String  FILENAME   = "query.wav";
    private static final String  INPUT      = "./query.wav";
    private static final String  KEY1       = "1ac04cd4347b49a2b89052edf1a45ef0";
    private static final String  ERROR_SEND = "I'm sorry, I could not process the voice command";
    private static final String  ERROR_RETRY= "I'm sorry, I could not understand that - please repeat";
    private static final float   CONFIDENCE_THREASHOLD =  0.4f;

    private GUI gui;
    private Detective detective;
    private TextToSpeech t2s = new TextToSpeech();
    private WolframQuery wq = new WolframQuery();


    Echo(){
        gui = new GUI();
        gui.addListener(this);

        detective = new Detective();
        detective.addListener(this);
    }

    String processSpeechToText(){
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
    String parseJsonFromMicrosoft(String json){
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

        gui.setAnswer();

        //Once the gui has been updated
        SwingUtilities.invokeLater(() -> {
            System.out.println("Invoking later");
            //Currently returning as json
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
            System.out.println(lengthOfFileSeconds);
            s.run();

            //Disable the GUI for X seconds
            try { sleep((long) lengthOfFileSeconds * 1000);
            } catch (InterruptedException e) { e.printStackTrace();}
            gui.setListen();
        });
    }
}

