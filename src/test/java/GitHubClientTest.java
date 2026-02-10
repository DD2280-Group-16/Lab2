import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class GitHubClientTest {

  @Test
  public void testJsonSuccess() {
    // We pass null because createJsonBody doesn't need the network client
    GitHubClient client = new GitHubClient(null, "fake_token");

    JSONObject json = client.createJsonBody(true, "fake_url");

    assertEquals("success", json.getString("state"));
    assertEquals("Build passed", json.getString("description"));
  }

  @Test
  public void testJsonFailure() {
    GitHubClient client = new GitHubClient(null, "fake_token");

    JSONObject json = client.createJsonBody(false, "fake_url");

    assertEquals("failure", json.getString("state"));
    assertEquals("Build failed", json.getString("description"));
  }

  @Test
  public void testUpdateCommitStatusSuccess() throws Exception {
    HttpClient mockClient = mock(HttpClient.class);
    HttpResponse<String> mockResponse = mock(HttpResponse.class);

    when(mockResponse.statusCode()).thenReturn(201);
    when(mockClient.send(any(), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

    GitHubClient client = new GitHubClient(mockClient, "fake_token");

    boolean res = client.notify("repoName", "123", true, "fake_url");
    assertTrue(res);
  }

  @Test
  public void testUpdateCommitStatusFailure() throws Exception {
    HttpClient mockClient = mock(HttpClient.class);
    HttpResponse<String> mockResponse = mock(HttpResponse.class);

    when(mockResponse.statusCode()).thenReturn(404);
    when(mockClient.send(any(), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

    GitHubClient client = new GitHubClient(mockClient, "fake_token");

    boolean res = client.notify("repoName", "123", false, "fake_url");
    assertFalse(res);
  }

  @Test
  public void testUpdateCommitStatusNetworkFailure() throws Exception {
    HttpClient mockClient = mock(HttpClient.class);

    when(mockClient.send(any(), any(HttpResponse.BodyHandler.class)))
        .thenThrow(new IOException("Network error"));

    GitHubClient client = new GitHubClient(mockClient, "fake_token");

    boolean res = client.notify("repoName", "123", false, "fake_url");
    assertFalse(res);
  }
}
