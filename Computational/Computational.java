import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.net.URLEncoder;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import org.json.JSONArray;


/*
 * Access the Wolfram Alpha computational knowledge engine. 
 * 
 * See http://products.wolframalpha.com/api/documentation.html
 *
 * David Wakeling, 2017.
 */
public class Computational {
  final static String PROBLEM = "What was the last species of animal to go extinct?";
  final static String APPID   = "J2LY8L-4EYJWU38EA";

  /*
   * Solve.
   */
  private static String solve( String input ) { 
    final String method = "POST";
    final String url    
      = ( "http://api.wolframalpha.com/v2/query"
        + "?" + "appid"  + "=" + APPID
        + "&" + "input"  + "=" + urlEncode( input )
        + "&" + "output" + "=" + "JSON"
        );
    final String[][] headers
      = { { "Content-Length", "0" }
        };
    final byte[] body = new byte[0];
    byte[] response   = HttpConnect.httpConnect( method, url, headers, body );
    String xml        = new String( response );
    return xml;
  } 

  /*
   * URL encode string.
   */ 
  private static String urlEncode( String s ) {
    try {
      return URLEncoder.encode( s, "utf-8" );
    } catch ( Exception ex ) {
      System.out.println( ex ); System.exit( 1 ); return null;
    }
  }

  /*
   * Solve problem giving solution.
   */
  public static void main( String[] argv ) {
      String solution = solve(PROBLEM);
      String preresult = "";

      try {
          JSONObject jo = new JSONObject(solution);

          JSONObject qr = ((JSONObject) jo.get("queryresult"));
          Boolean suc = (Boolean) qr.get("success");
          if (suc) {
              JSONArray pods = (JSONArray) qr.get("pods");
              JSONObject pod2 = (JSONObject) pods.get(1);
              JSONArray subpod = (JSONArray) pod2.get("subpods");

              JSONObject subsubpod = (JSONObject) subpod.get(0);
              preresult = (String) subsubpod.get("plaintext");

          } else {
              System.out.println("I didn't understand the question, moron");
          }

      }catch (Exception e) {
          System.out.println("JSON processing failed : " + e);
      }
      if (preresult.startsWith("1 | noun | ")) {
          preresult = preresult.substring(11, preresult.indexOf("2 | noun |"));
      } else if (preresult.startsWith("noun | ")) {
          preresult = preresult.substring(7);
      }

      //JSONArray fin = (JSONArray)subpod.get("plaintext");
      //System.out.println(fin);

      System.out.println(preresult);
      //System.out.println( solution );
  }
}
