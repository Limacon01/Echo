package echo.Computational;

import java.net.URLEncoder;
import echo.Networking.*;

/*
* Access the Wolfram Alpha computational knowledge engine.
*/
class WolframQuery {
    private final static String APPID   = "J2LY8L-4EYJWU38EA";

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

    /**
     *
     * @param s is the string to process
     * @return the processed string
     */
    private String processJson(String s) {
        if (!s.substring(20,36).equals("\"success\" : true")) {
            return "Query unsuccessful";
        }
        if (!s.contains("\"title\" : \"Result\"")) {
            return "No results found";
        }

        int resultPodIndex = s.indexOf("\"title\" : \"Result\"");
        if (!s.substring(resultPodIndex).contains("\"plaintext\" : ")) {
            return "No results found";
        }
        String resultPod = s.substring(resultPodIndex);
        String resultString = resultPod.substring(resultPod.indexOf("\"plaintext\" : ") + 15);

        int endResultString = resultString.indexOf("\"");
        String finalResult = resultString.substring(0, endResultString);

        return finalResult;
    }

    /*
    * Solve problem giving solution.
    */
    public String processQuestion( String question ) {
        String solution = solve(question);
        String processedSolution = processJson(solution);
        //System.out.println(processedSolution);
        return processedSolution;
    }
}






















