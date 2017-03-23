package echo.Tests.ComputationalTests;

import echo.Computational.BackgroundWorker;
import echo.GUI;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @version 2.0
 *
 * This class tests the backgroundWorker functions as expected
 * -Stopping when it should
 * -Correctly parses JSON
 *
 */
public class BackgroundWorkerTest {
    /**
     * @throws Exception    Throws InterruptedException()
     *                      when interrupted using  bgw.cancel(true)
     */
    @Test
    public void checkForInterrupt() throws Exception {
        GUI g = new GUI();
        BackgroundWorker bgw = new BackgroundWorker(g);
        bgw.cancel(true);
        assertEquals("Bgw should stop", bgw.isCancelled(), true);
    }

    @Test
    public void parseJsonFromMicrosoft() throws Exception {
        String exampleJSON = "{\"version\":\"3.0\",\"header\":{\"status\":\"success\",\"scenario\":\"smd\",\"name\":\"Test 123.\",\"lexical\":\"test one two three\",\"properties\":{\"requestid\":\"8c8a7c8a-a135-457c-a3d1-7b45f42ea36d\",\"HIGHCONF\":\"1\"}},\"results\":[{\"scenario\":\"smd\",\"name\":\"Test 123.\",\"lexical\":\"test one two three\",\"confidence\":\"0.9052536\",\"properties\":{\"HIGHCONF\":\"1\"}}]}";
        String results = BackgroundWorker.parseJsonFromMicrosoft(exampleJSON);
        assertEquals("Should find 'Test 123.'", results, "Test 123.");
    }

}