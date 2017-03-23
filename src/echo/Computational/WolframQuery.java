package echo.Computational;

import java.net.URLEncoder;
import java.util.ArrayList;

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
     * @return          URL string encoded
     */
    private static String urlEncode( String s ) {
        try {
          return URLEncoder.encode( s, "utf-8" );
        } catch ( Exception ex ) {
          ex.printStackTrace(); System.exit( 1 ); return null;
        }
    }

    /**
     * Search for a plaintext answer within a JSON formatted answer. This method looks for
     * specific answers, namely: results, values, responses, definitions and input
     * interpretations
     * @param   s       Unprocessed JSON string
     * @return          Either the query result or a message describing an unsuccessful query
     *                  in plaintext format
     */
    private String processJson(String s) {
        int resultPodIndex = 0;

        if (!s.substring(20,36).equals("\"success\" : true")) {
            return "Query unsuccessful";
        }
        ArrayList<String> podStrings = new ArrayList<>();
        String titleString = "\"title\" : ";
        podStrings.add("\"Result\"");
        podStrings.add("\"Value\"");
        podStrings.add("\"Response\"");
        podStrings.add("\"Definitions\"");
        podStrings.add("\"Input interpretation\"");

        int i;
        for (i=0; i<podStrings.size(); i++) {
            if (s.contains(titleString + podStrings.get(i))) {
                resultPodIndex = s.indexOf(titleString + podStrings.get(i));
                break;
            }
        }
        if (resultPodIndex == 0 || !s.substring(resultPodIndex).contains("\"plaintext\" : ")) {
            return "No results found ";
        }
        String resultPod = s.substring(resultPodIndex);
        String resultString = resultPod.substring(resultPod.indexOf("\"plaintext\" : ") + 15);
        int endResultString = resultString.indexOf("\"");
        //May contain multiple definitions, interpretations etc.:
        String longResult = resultString.substring(0, endResultString);
        String shortResult = longResult.replaceAll("noun", "");
        //shortResult = shortResult.split("\\n")[0];
        if (shortResult.contains("\\n")) {
            shortResult = shortResult.substring(0, shortResult.indexOf("\\n"));
        }
        return shortResult;
    }

    /**
     * Attempt to find a solution to a question
     * @param   question    User question
     * @return              A processed solution or message describing an unsuccessful query
     */
    public String processQuestion( String question ) {
        String solution = solve(question);
        return processJson(solution);
    }
}






















