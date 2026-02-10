import org.json.JSONException;
import org.json.JSONObject;

public class PayloadParser {
  public final String sshUrl;
  public final String repoName;
  public final String commitHash;
  public final String branch;

  /**
   * Constructor parses the JSON string immediately.
   *
   * @param payload The raw JSON string from the request body.
   * @throws JSONException if required fields are missing.
   */
  public PayloadParser(String payload) {
    JSONObject json = new JSONObject(payload);

    if (!json.has("ref")) {
      throw new IllegalArgumentException("Invalid payload: missing 'ref' field");
    }

    this.branch = json.getString("ref").replace("refs/heads/", "");
    this.commitHash = json.getString("after");

    if (json.has("repository")) {
      JSONObject repo = json.getJSONObject("repository");
      this.sshUrl = repo.getString("ssh_url");
      this.repoName = repo.getString("full_name");
    } else {
      throw new IllegalArgumentException("Invalid payload: missing 'repository' object");
    }
  }
}

