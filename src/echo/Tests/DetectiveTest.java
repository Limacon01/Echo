package echo.Tests;

import echo.Computational.Detective;
import echo.Echo;
import echo.EchoApp;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * version 1.0
 *
 * Tests addListener method in Detective Class
 */
public class DetectiveTest {

    /**
     * Creates a new detective and asserts that the array of
     * soundDetectedListeners is equal to 0 to begin with.
     * Then this test adds a listener to the
     * soundDetectedListener array and assert it contains
     * 1 listener.
     */
    @Test
    public void TestAddListener(){
        Detective detective = new Detective();
        Echo echo = new Echo(new EchoApp());
        assertEquals(detective.soundDetectedListeners.size(),0);
        detective.addListener(echo);
        assertEquals(detective.soundDetectedListeners.size(),1);

    }

    /**
     * @throws Exception
     */
    @Test
    public void run() throws Exception {
        Detective detective = new Detective();
    }

}