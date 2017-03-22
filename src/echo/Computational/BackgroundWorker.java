package echo.Computational;

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
    GUI updatedGUI;

    private Detective sherlock;

    public BackgroundWorker(GUI gui){
        this.updatedGUI = gui;
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

        publish("honk");
        failIfInterrupted();

        int seconds = 4;
        sleep(seconds*1000);

        publish("done with sleeping - NAP OVER BITCHES");

        //Record sound
        //ProcessSpeechToText
        //ProcessQuestion
        //Output speechToFile

//        //Record sound for 5s
//        System.out.println("Sound detected");
//        AudioInputStream ais = RecordSound.setupStream();
//        RecordSound.recordSound(FILENAME, RecordSound.readStream(ais));
//        RecordSound.closeDataLine();
//
//        gui.setAnswer();
//
//        //Once the gui has been updated
//        SwingUtilities.invokeLater(() -> {
//            //Currently returning as json
//            String toSendToWolfram = processSpeechToText();
//            File outputFile;
//            if (toSendToWolfram.equals("")) {
//                //Create appropriate file
//                outputFile = t2s.outputSpeechToFile(ERROR_SEND);
//                System.out.println("attempting to play error message");
//            } else {
//                //Send to wolfram
//                String result = wq.processQuestion(toSendToWolfram);
//
//                //Create appropriate file
//                outputFile = t2s.outputSpeechToFile(result);
//            }
//
//            //Play text-to-speech
//            Sounds s = new Sounds(outputFile);
//            double lengthOfFileSeconds = s.getLengthOfFile(outputFile) + 2;
//            System.out.println(lengthOfFileSeconds);
//            s.run();
//
//            //Disable the GUI for X seconds
//            try { sleep((long) lengthOfFileSeconds * 1000);
//            } catch (InterruptedException e) { e.printStackTrace();}
//
//            if(gui.hasBeenClicked()){
//                gui.setOff();
//
//            }
//            gui.setListen();
//        });

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
        System.out.println("Hopefully this doesnt explode");
    }
}
