import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
/*
 * Text to speech conversion using Microsoft Cognitive Services.
 *
 * See http://www.microsoft.com/cognitive-services/en-us/speech-api
 *
 * David Wakeling, 2017.
 */
public class TextToSpeech {
  final static String TEXT   = "Frankly, my dear, I don't give a damn";
  final static String LANG   = "en-US";
  final static String GENDER = "Female";
  final static String OUTPUT = "output.wav";
  final static String FORMAT = "riff-16khz-16bit-mono-pcm";

  /*final static String KEY1   = "b750f20fdc0c4ac8aaf59073d98c1a05";*/
   final static String KEY2   = "1ac04cd4347b49a2b89052edf1a45ef0";

  /*
   * Renew an access token --- they expire after 10 minutes.
   */
  static String renewAccessToken( String key1 ) {
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

  /*
   * Synthesize speech.
   */
  static byte[] synthesizeSpeech( String token, String text
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

  /*
   * Write data to file.
   */
  static void writeData( byte[] buffer, String name ) {
    try {
      File             file = new File( name );
      FileOutputStream fos  = new FileOutputStream( file );
      DataOutputStream dos  = new DataOutputStream( fos );
      dos.write( buffer );
      dos.flush();
      dos.close();
    } catch ( Exception ex ) {
      System.out.println( ex ); System.exit( 1 ); return;
    }
  }

  /*
   * Convert text to speech.
   */
  public static void main( String[] argv ) {
    final String token  = renewAccessToken( KEY2 );
    final byte[] speech = synthesizeSpeech( token, TEXT, LANG, GENDER, FORMAT );
    writeData( speech, OUTPUT );
  }
}
