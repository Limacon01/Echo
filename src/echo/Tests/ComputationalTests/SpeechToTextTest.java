package echo.Tests.ComputationalTests;

import echo.Computational.SpeechToText;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by Mark on 09/03/2017.
 */
public class SpeechToTextTest {
    @Test
    public void renewAccessToken() throws Exception {
        final String  KEY1       = "1ac04cd4347b49a2b89052edf1a45ef0";
        String token = SpeechToText.renewAccessToken(KEY1);
        System.out.println(token);
    }

    @Test
    public void recognizeSpeech() throws Exception {
        //{"version":"3.0","header":{"status":"success","scenario":"smd","name":"Test 123.","lexical":"test one two three","properties":{"requestid":"d1a1c9d4-d503-42f2-9a3f-50e12ba7fc43","HIGHCONF":"1"}},"results":[{"scenario":"smd","name":"Test 123.","lexical":"test one two three","confidence":"0.9431151","properties":{"HIGHCONF":"1"}}]}
        final String ExampleOutput = "{\"version\":\"3.0\",\"header\":{\"status\":\"success\",\"scenario\":\"smd\",\"name\":\"Test 123.\",\"lexical\":\"test one two three\",\"properties\":{\"requestid\":\"d1a1c9d4-d503-42f2-9a3f-50e12ba7fc43\",\"HIGHCONF\":\"1\"}},\"results\":[{\"scenario\":\"smd\",\"name\":\"Test 123.\",\"lexical\":\"test one two three\",\"confidence\":\"0.9431151\",\"properties\":{\"HIGHCONF\":\"1\"}}]}";
        final String  KEY1       = "1ac04cd4347b49a2b89052edf1a45ef0";
        URL testFile = this.getClass().getResource("/echo/Tests/Test.wav");

        String token  = SpeechToText.renewAccessToken( KEY1 );
        byte[] speech = SpeechToText.readData( testFile.getPath() );
        final String jsonText   = SpeechToText.recognizeSpeech( token, speech );

        //Tests are a work in progress
        System.out.println(jsonText);
        System.out.println(jsonText.indexOf(" Test 123. "));
        //assertEquals(jsonText.substring())
        //assertEquals(jsonText, ExampleOutput);
    }

    @Test
    public void readData() throws Exception {

    }

}