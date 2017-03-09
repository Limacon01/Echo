package echo.Tests;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import echo.Computational.WolframQuery;
import org.junit.Test;

import java.lang.reflect.*;

import static org.junit.Assert.*;

/**
 * @author James
 * @version 1.0
 * Testing methods for WolframQuery Class
 */
public class WolframQueryTest {
    private final static String sampleQuestion1 = "What is the melting point of copper?";
    private final static String sampleAnswer1 = "1084.62 °C  (degrees Celsius)";

    /**
     * Test that solve method successfully retrieves a JSON String from Wolfram Alpha's
     * Knowledge engine.
     * This test is also dependent on:
     *  - httpConnect method functioning correctly
     *  - an online connection
     *  - Wolfram Alpha's servers functioning correctly
     */
    @Test
    public void testSolve() {
        echo.Computational.WolframQuery wq = new WolframQuery();
        Class c = wq.getClass();

        // Expected response:
        final String jsonRespons1 = "{\"queryresult\" : {\n" +
                "	\"success\" : true, \n" +
                "	\"error\" : false, \n";
        // Invoke private method solve:
        try {
            Method solve = c.getDeclaredMethod("solve", String.class);
            solve.setAccessible(true);
            String result = (String)solve.invoke(wq, sampleQuestion1);

            // Cannot compare full json files as the urls and timings change
            // Instead, just check that the query was a success and that there were no errors raised
            String resultHead = result.substring(0, jsonRespons1.length());
            assertEquals(resultHead, jsonRespons1);
        } catch(NoSuchMethodException e) {
            System.out.println(e.toString());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test the entire question processing procedur from
     * an input String to a processed result from Wolfram Alpha
     */
    @Test
    public void testProcessQuestion() {
        WolframQuery wq = new WolframQuery();
        String result = wq.processQuestion(sampleQuestion1);
        assertEquals(sampleAnswer1, result);
    }

}