package echo.Tests.ComputationalTests;

import echo.Computational.TextToSpeech;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * version 1.0
 * Test that renewAccessToken() in text to speech renews the token for a key
 */

public class TextToSpeechTest {
    @Test
    public void renewAccessToken() throws Exception{
        final String KEY2   = "1ac04cd4347b49a2b89052edf1a45ef0";
        String token = TextToSpeech.renewAccessToken(KEY2);
        System.out.println(token);
        assertTrue(!token.equals(null));
    }

}