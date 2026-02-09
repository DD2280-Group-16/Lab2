import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONTokener;

import jakarta.servlet.http.HttpServletRequest;


public class PayloadParser {

    String sshUrl;

    /**
     * Parses the JSON body of an HttpServletRequest into a JSONObject.
     */
    public void parse(HttpServletRequest request) throws IOException {
        // The JSONTokener can consume the input stream directly
        // which is more efficient than reading it into a String first.
        JSONTokener tokener = new JSONTokener(request.getInputStream());
        JSONObject payload = new JSONObject(tokener);
        if (payload.has("repository")) {
            JSONObject repo = payload.getJSONObject("repository");
            set_sshUrl(repo.getString("ssh_url"));
        }

    }

    public void set_sshUrl(String sshUrl){
        this.sshUrl = sshUrl;
    }
}