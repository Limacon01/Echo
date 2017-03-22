package echo;

/**
 * Main controller for the Echo device
 */
public class Echo {
    private static final float   CONFIDENCE_THREASHOLD =  0.4f;

    private GUI gui;

    Echo(){
        gui = new GUI();
    }

    /**
     * This function takes a recorded question string in json format
     * and returns the processed result string as long as Microsoft's
     * cognitive service's confidence threshold is above a defined value
     * @param json  input string
     * @return
     */
    public static String parseJsonFromMicrosoft(String json){
        String succStr = "\"status\":\"success\"";
        int status = json.indexOf(succStr);

        //If we get a success
        if(status != -1){
            // and confidence is above 0.4
            String confidenceString = "\"confidence\":\"";
            int confidenceThreshold = json.indexOf(confidenceString) + confidenceString.length();
            float foundThreshold = Float.parseFloat(json.substring(confidenceThreshold, confidenceThreshold+5));
            if(foundThreshold > CONFIDENCE_THREASHOLD){
                //We're sure this the question string
                String startOfQuestion = "\"name\":\"";
                String endOfQuestion = "\",\"lexical\":";

                int questionIndex = json.indexOf(startOfQuestion) + startOfQuestion.length();
                int endQuestionIn = json.indexOf(endOfQuestion);
                String parsedString = json.substring(questionIndex, endQuestionIn);
                System.out.println(parsedString);
                return parsedString;
            }
        }
        return "";
    }
}

