import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import scripts.CompileScript;
import scripts.ScriptResult;
import scripts.TestScript;

/**
 * Skeleton of a ContinuousIntegrationServer which acts as webhook
 * See the Jetty documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler {

  /**
   * Compiles the project at projectPath. Returns the result (exit code + output).
   */
  public ScriptResult handleCompilation(String projectPath) throws IOException, InterruptedException {
    return new CompileScript(projectPath).run();
  }

  /**
   * Runs tests on the project at projectPath. Returns the result (exit code + output).
   */
  public ScriptResult handleTests(String projectPath) throws IOException, InterruptedException {
    return new TestScript(projectPath).run();
  }

  @Override
  public void handle(String target,
      Request baseRequest,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException, ServletException {
    response.setContentType("text/html;charset=utf-8");
    response.setStatus(HttpServletResponse.SC_OK);
    baseRequest.setHandled(true);

    System.out.println(target);

    // here you do all the continuous integration tasks
    // for example
    // 1st clone your repository
    // 2nd compile the code

    response.getWriter().println("CI job done");
  }

  // used to start the CI server in command line
  public static void main(String[] args) throws Exception {
    Server server = new Server(8080);
    server.setHandler(new ContinuousIntegrationServer());
    server.start();
    server.join();
  }
}
