import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONTokener;

import jakarta.servlet.http.HttpServletRequest;

public class PayloadParser {

    /**
     * Parses the JSON body of an HttpServletRequest into a JSONObject.
     */
    public static JSONObject parse(HttpServletRequest request) throws IOException {
        // The JSONTokener can consume the input stream directly
        // which is more efficient than reading it into a String first.
        JSONTokener tokener = new JSONTokener(request.getInputStream());
        return new JSONObject(tokener);
    }
}