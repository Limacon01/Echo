package echo.Tests.ComputationalTests;

import echo.Computational.Detective;
import echo.Echo;
import echo.EchoApp;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by James on 22/03/2017.
 */
public class DetectiveTest {
    /**
     * Creates a new Audio Format using Detective's final static
     * attributes. Checks that an Audio Format is actually created.
     */
    @Test
    public void testCreateAudioFormat() {
        Detective detective = new Detective();
        AudioFormat af = detective.createAudioFormat();
        assertNotNull(af);
    }

    /**
     * Creates a new DataLine and checks that its buffer size and
     * Audio Format have been set up correctly.
     */
    @Test
    public void testCreateDataLine() {
        Detective detective = new Detective();
        final int bufferSize = 2048;

        AudioFormat af = detective.createAudioFormat();
        DataLine dl = detective.createDataLine(bufferSize, af);
        assertEquals(bufferSize, dl.getBufferSize());
        assertEquals(af, dl.getFormat());
    }

}