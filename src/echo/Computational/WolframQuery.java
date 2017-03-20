package echo.Computational;

import java.net.URLEncoder;
import echo.Networking.*;

/**
 * @version 1.4
 * Methods for accessing the Wolfram Alpha Computational Knowledge Engine
 */
public class WolframQuery {
    private final static String APPID   = "J2LY8L-4EYJWU38EA";


    /**
     * Queries Wolfram Alpha with a question
     * @param   input   Question string
     * @return          Answer string in JSON format
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
        return new String( response );
    }

    /**
     * Encode String to utf-8
     * @param   s       Input testing
     * @return
     */
    private static String urlEncode( String s ) {
        try {
          return URLEncoder.encode( s, "utf-8" );
        } catch ( Exception ex ) {
          System.out.println( ex ); System.exit( 1 ); return null;
        }
    }

    /**
     * Search for a plaintext answer within a JSON formatted answer
     * @param   s       Unprocessed JSON string
     * @return          Either the query result or a message describing an unsuccessful query
     *                  in plaintext format
     */
    private String processJson(String s) {
        int resultPodIndex;

        if (!s.substring(20,36).equals("\"success\" : true")) {
            return "Query unsuccessful";
        }

        if (s.contains("\"title\" : \"Result\"")) {
            resultPodIndex = s.indexOf("\"title\" : \"Result\"");
        } else if (s.contains("\"title\" : \"Value\"")) {
            resultPodIndex = s.indexOf("\"title\" : \"Value\"");
        } else {
            return "No results found ";
        }

        if (!s.substring(resultPodIndex).contains("\"plaintext\" : ")) {
            return "No results found ";
        }
        String resultPod = s.substring(resultPodIndex);
        String resultString = resultPod.substring(resultPod.indexOf("\"plaintext\" : ") + 15);
        int endResultString = resultString.indexOf("\"");
        return resultString.substring(0, endResultString);
    }

    /**
     * Attempt to find a solution to a question
     * @param   question    User question
     * @return              A processed solution or message describing an unsuccessful query
     */
    public String processQuestion( String question ) {
        String solution = solve(question);
        System.out.println("\n -- " + solution + " -- \n");
        return processJson(solution);
    }
}






















