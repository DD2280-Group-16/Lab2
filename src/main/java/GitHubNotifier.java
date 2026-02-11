import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class GitHubNotifier {
  private HttpClient client;
  private final String accessToken;

  public GitHubNotifier(HttpClient client, String accessToken) {
    this.client = client;
    this.accessToken = accessToken;
  }

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

      System.out.println("GitHub status update: " + response.statusCode());
      return response.statusCode() == 201;

    } catch (Exception e) {
      System.err.println("Failed to update GitHub status: " + e.getMessage());
      return false;
    }
  }

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
