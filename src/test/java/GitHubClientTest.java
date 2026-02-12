import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import org.json.JSONObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link GitHubNotifier}: JSON body for success/failure, and notify on 201 vs other status or network error.
 */
class GitHubClientTest {

/**
 * Tests that the method generates a JSON object with the correct fields when the build is successful
 */
  @Test
  public void testJsonSuccess() {
    // We pass null because createJsonBody doesn't need the network client
    GitHubNotifier client = new GitHubNotifier(null, "fake_token");

    JSONObject json = client.createJsonBody(true, "fake_url");

    assertEquals("success", json.getString("state"));
    assertEquals("Build passed", json.getString("description"));
  }

/**
 * Tests that the method correctly generates a JSON object representing a failed build
 */
  @Test
  public void testJsonFailure() {
    GitHubNotifier client = new GitHubNotifier(null, "fake_token");

    JSONObject json = client.createJsonBody(false, "fake_url");

    assertEquals("failure", json.getString("state"));
    assertEquals("Build failed", json.getString("description"));
  }

/**
 * Tests that GitHubNotifier successfully updates the commit status for a 201 status code
 *
 * @throws Exception
 */
  @Test
  public void testUpdateCommitStatusSuccess() throws Exception {
    HttpClient mockClient = mock(HttpClient.class);
    HttpResponse<String> mockResponse = mock(HttpResponse.class);

    when(mockResponse.statusCode()).thenReturn(201);
    when(mockClient.send(any(), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

    GitHubNotifier client = new GitHubNotifier(mockClient, "fake_token");

    boolean res = client.notify("repoName", "123", true, "fake_url");
    assertTrue(res);
  }

/**
 * Tests that the GitHubNotifier correctly handles a failure response 
 * 
 * @throws Exception 
 */
  @Test
  public void testUpdateCommitStatusFailure() throws Exception {
    HttpClient mockClient = mock(HttpClient.class);
    HttpResponse<String> mockResponse = mock(HttpResponse.class);

    when(mockResponse.statusCode()).thenReturn(404);
    when(mockClient.send(any(), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

    GitHubNotifier client = new GitHubNotifier(mockClient, "fake_token");

    boolean res = client.notify("repoName", "123", false, "fake_url");
    assertFalse(res);
  }

/**
 * Tests that the method returns false for a network failure occurs
 * 
 * @throws Exception
 */
  @Test
  public void testUpdateCommitStatusNetworkFailure() throws Exception {
    HttpClient mockClient = mock(HttpClient.class);

    when(mockClient.send(any(), any(HttpResponse.BodyHandler.class)))
        .thenThrow(new IOException("Network error"));

    GitHubNotifier client = new GitHubNotifier(mockClient, "fake_token");

    boolean res = client.notify("repoName", "123", false, "fake_url");
    assertFalse(res);
  }
}
