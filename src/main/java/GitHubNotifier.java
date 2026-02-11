import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

/**
 * Sends build status notifications to GitHub for a specific commit
 */
public class GitHubNotifier {
  private HttpClient client;
  private final String accessToken;

  public GitHubNotifier(HttpClient client, String accessToken) {
    this.client = client;
    this.accessToken = accessToken;
  }

/**
 * Sends a status notification to a GitHub repository for a specific commit SHA.
 *
 * @param repoName   the name of the repository
 * @param sha        the commit SHA
 * @param success    whether the status is successful (true) or failed (false)
 * @param targetUrl  the URL to the build page
 * @return           true if the status update was successful, false otherwise
 */
  public boolean notify(String repoName, String sha, boolean success, String targetUrl) {
    String url = "https://api.github.com/repos/" + repoName + "/statuses/" + sha;

    JSONObject body = createJsonBody(success, targetUrl);

    try {
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(url))
              .header("Authorization", "Bearer " + accessToken)
              .header("Accept", "application/vnd.github+json")
              .header("Content-Type", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
              .build();

      HttpResponse<String> response =
          this.client.send(request, HttpResponse.BodyHandlers.ofString());

      return response.statusCode() == 201;

    } catch (Exception e) {
      return false;
    }
  }

/**
 * Creates a JSON object representing the status of the build
 *
 * @param success   Indicates whether the build was successful
 * @param targetUrl The URL to link to for more details about the build
 * @return          A JSONObject containing the build state, description, context, and target URL
 */
  public JSONObject createJsonBody(boolean success, String targetUrl) {
    String state = success ? "success" : "failure";
    String desc = success ? "Build passed" : "Build failed";
    JSONObject body = new JSONObject();

    body.put("state", state);
    body.put("description", desc);
    body.put("context", "continous-integration/java-server");
    body.put("target_url", targetUrl);

    return body;
  }
}
