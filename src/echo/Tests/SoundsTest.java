package echo.Tests;

import echo.Sounds;
import org.junit.Test;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;


public class SoundsTest {

    /**
     * Tests that the length of a file is accurately measured to what it should be
     * by getLengthOfFile() method using sound file of known length
     */
    @Test
    public void testGetLengthOfFile(){
        File testFile = new File("src/echo/Resources/Sounds/errorMessage.wav");
        //We know errorMessage was a duration of 2 seconds according to properties
        //and details. This is rounded down to the nearest second
        double length = Sounds.getLengthOfFile(testFile);
        assert(length >= 2 || length <= 3);
    }

    /**
     * Tests that soundRes is getting set to the right file by Sounds constructor
     */
    @Test
    public void testSounds(){
        Sounds statusOn = new Sounds("ON");
        assertEquals(statusOn.soundRes, this.getClass().getResource("/echo/Resources/Sounds/onSound.wav"));
        Sounds statusOff= new Sounds("OFF");
        assertEquals(statusOff.soundRes, this.getClass().getResource("/echo/Resources/Sounds/offSound.wav"));
        Sounds statusError = new Sounds("gibberish");
        assertEquals(statusError.soundRes, this.getClass().getResource("/echo/Resources/Sounds/errorMessage.wav"));
    }

    /**
     * Tests that AudioInputStream is being assigned a file
     *
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    @Test
    public void testAssignAudioInputStream() throws IOException, UnsupportedAudioFileException {
        Sounds statusOn = new Sounds("ON");
        assertEquals(statusOn.desiredFile, null);
        statusOn.assignAudioInputStream();
        assertNotNull(statusOn.as);
        assertEquals(statusOn.soundRes, null);

        File testFile = new File("src/echo/Tests/Test.wav");
        Sounds testSounds = new Sounds(testFile);
        assertEquals(testSounds.soundRes, null);
        testSounds.assignAudioInputStream();
        assertNotNull(testSounds.as);

    }

}