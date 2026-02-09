import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONTokener;

import jakarta.servlet.http.HttpServletRequest;


public class PayloadParser {

    String sshUrl;
    String repoName;
    String commitHash;
    String branch;

    /**
     * Parses the JSON body of an HttpServletRequest into a JSONObject.
     */
    public void parse(HttpServletRequest request) throws IOException {
        // The JSONTokener to break down the request.
        JSONTokener tokener = new JSONTokener(request.getInputStream());
        JSONObject payload = new JSONObject(tokener);
        if (!payload.has("ref")) {
            throw new IllegalArgumentException("Missing required field: ref");
        }
        if (payload.has("repository")) {
            JSONObject repo = payload.getJSONObject("repository");
            setSshUrl(repo.getString("ssh_url"));
            setRepoName(repo.getString("full_name"));
            setCommitHash(payload.getString("after"));
            setBranch(payload.getString("ref"));
        }

    }


    // Setters
    public void setSshUrl(String sshUrl){
        this.sshUrl = sshUrl;
    }
    public void setRepoName(String repoName){
        this.repoName = repoName;
    }
    public void setCommitHash(String commitHash){
        this.commitHash = commitHash;
    }
    public void setBranch(String ref){
        String branchName = ref.replace("refs/heads/", "");
        this.branch = branchName;
    }
}