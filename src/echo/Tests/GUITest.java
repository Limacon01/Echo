package echo.Tests;

import echo.GUI;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the GUI, specifically its state change methods
 */
public class GUITest {
    @Test
    public void testSetOff() throws Exception {
        GUI gui = new GUI();
        gui.setOff();
        assertEquals("OFF", gui.status);
    }

    @Test
    public void testSetListen() throws Exception {
        GUI gui = new GUI();
        gui.firstTime = true;
        gui.setListen();
        assertEquals("LISTEN", gui.status);
        assertEquals(false, gui.firstTime);
    }

    @Test
    public void testSetAnswer() throws Exception {
        GUI gui = new GUI();
        gui.setAnswer();
        assertEquals("ANSWER", gui.status);
    }

    @Test
    public void testSetStatus() throws Exception {
        GUI gui = new GUI();
        gui.status = "OFF";
        gui.setStatus("LISTEN");
        assertEquals("LISTEN", gui.status);
    }

    @Test
    public void testSetFirstTime() throws Exception {
        GUI gui = new GUI();
        gui.firstTime = false;
        gui.setFirstTime();
        assertEquals(true, gui.firstTime);
    }

}