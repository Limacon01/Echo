package echo.Computational;

import echo.Networking.HttpConnect;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @version 1.0
 * @author David Wakeling
 *
 * Text to speech using Microsoft Cognition Services.
 */
public class TextToSpeech {
    final static String LANG   = "en-US";
    final static String GENDER = "Female";
    final static String OUTPUT = "./speechOutput.wav";
    final static String FORMAT = "riff-16khz-16bit-mono-pcm";

    /*final static String KEY1   = "b750f20fdc0c4ac8aaf59073d98c1a05";*/
    final static String KEY2   = "1ac04cd4347b49a2b89052edf1a45ef0";

    public File outputSpeechToFile(String text){
        final String token  = renewAccessToken( KEY2 );
        final byte[] speech = synthesizeSpeech( token, text, LANG, GENDER, FORMAT );
        return writeData( speech, OUTPUT );
    }

    /**
     * Renew an access token --- they expire after 10 minutes.
     * @param key1          Developer key
     * @return              Renewed access token
     */
    public static String renewAccessToken( String key1 ) {
        final String method = "POST";
        final String url =
                "https://api.cognitive.microsoft.com/sts/v1.0/issueToken";
        final byte[] body = {};
        final String[][] headers
                = { { "Ocp-Apim-Subscription-Key", key1                          }
                , { "Content-Length"           , String.valueOf( body.length ) }
        };
        byte[] response = HttpConnect.httpConnect( method, url, headers, body );
        return new String( response );
    }

    /**
     * Synthesize speech.
     * @param token         Authorised access token
     * @param text          Text to synthesize into speech
     * @param lang          Locale to synthesize speech in
     * @param gender        Preferred gender of voice
     * @param format        Audio output format
     * @return              Byte array of speech
     */
    public static byte[] synthesizeSpeech( String token, String text
            , String lang,  String gender
            , String format ) {
        final String method = "POST";
        final String url = "https://speech.platform.bing.com/synthesize";
        final byte[] body
                = ( "<speak version='1.0' xml:lang='en-us'>"
                + "<voice xml:lang='" + lang   + "' "
                + "xml:gender='"      + gender + "' "
                + "name='Microsoft Server Speech Text to Speech Voice"
                + " (en-US, ZiraRUS)'>"
                + text
                + "</voice></speak>" ).getBytes();
        final String[][] headers
                = { { "Content-Type"             , "application/ssml+xml"        }
                , { "Content-Length"           , String.valueOf( body.length ) }
                , { "Authorization"            , "Bearer " + token             }
                , { "X-Microsoft-OutputFormat" , format                        }
        };
        byte[] response = HttpConnect.httpConnect( method, url, headers, body );
        return response;
    }

    /**
     * Write data to file.
     * @param buffer        Byte array of speech
     * @param name          Desired name of output file
     * @return              Output audio file
     */
    public static File writeData( byte[] buffer, String name ) {
        try {
            File             file = new File( name );
            FileOutputStream fos  = new FileOutputStream( file );
            DataOutputStream dos  = new DataOutputStream( fos );
            dos.write( buffer );
            dos.flush();
            dos.close();
            return file;
        } catch ( Exception ex ) {
            System.out.println( ex ); System.exit( 1 ); return null;
        }
    }
}