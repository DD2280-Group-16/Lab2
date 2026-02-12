
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import functions.BranchCloner;
import functions.DefaultProcessRunner;
import functions.DirectoryRemover;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import scripts.MavenTestExecutor;


/**
 * Implements a (CI) server with Jetty
 */
public class ContinuousIntegrationServer extends AbstractHandler {

    private final Dotenv dotenv = Dotenv.load();
    private final String accessToken = dotenv.get("GITHUB_ACCESS_TOKEN");
    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Handles incoming HTTP requests for the Continuous Integration Server.
     *
     * @param target the target URL path of the request
     * @param baseRequest the Jetty base request object
     * @param request the servlet request object
     * @param response the servlet response object
     * @throws IOException if an input or output error occurs
     * @throws ServletException if a servlet error occurs
     */
    public void handle(
            String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println("Request received: " + target + " " + request.getMethod());

        // We recieve a Webhook
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            BufferedReader reader = request.getReader();
            String payload = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            try {
                // Parse payload
                PayloadParser parser = new PayloadParser(payload);
                // Clone and run the tests
                processBuild(parser);
                System.out.println("Done");

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if ("GET".equalsIgnoreCase(request.getMethod())) {
            File historyDir = new File("build_history");

            // Render the logs for `our-website/builds/*`
            if (target.startsWith("/builds/")) {
                String commitHash = target.replace("/builds/", "");
                // Get the logs for that commit
                File logFile = new File(historyDir, commitHash + "/log.txt");

                if (logFile.exists()) {
                    response.getWriter().println("<h1>Build Log for " + commitHash + "</h1>");
                    response.getWriter().println("<pre>"); // <pre> preserves the log formatting
                    response.getWriter().println(Files.readString(logFile.toPath()));
                    response.getWriter().println("</pre>");
                    response.getWriter().println("<br><a href='/'>Back to List</a>");
                } else {
                    response.getWriter().println("<h1>404 - Log Not Found</h1>");
                    response.getWriter().println("<p>Build " + commitHash + " does not exist</p>");
                }
            } else {
                // Render the "homepage"
                response.getWriter().println("<h1>Build History</h1>");

                // Render the list of builds
                File[] builds = historyDir.listFiles(File::isDirectory);
                response.getWriter().println("<ul>");
                if (builds != null) {
                    for (File build : builds) {
                        String hash = build.getName();
                        // Create a link to /builds/{hash}
                        response
                                .getWriter()
                                .println("<li><a href='/builds/" + hash + "'>Build " + hash + "</a></li>");
                    }
                }
                response.getWriter().println("</ul>");
            }
        }
    }

    /**
     * Processes a build request by cloning the specified branch, running tests, and notifying
     *
     * @param parser The PayloadParser containing repository and commit information
     */
    private void processBuild(PayloadParser parser) {
        DefaultProcessRunner runner = new DefaultProcessRunner();
        BranchCloner cloner = new BranchCloner(runner);
        DirectoryRemover cleaner = new DirectoryRemover();
        GitHubNotifier gitHubClient = new GitHubNotifier(httpClient, accessToken);
        EmailNotifier emailNotifier = new EmailNotifier();

        Path temporaryDir = null;
        String logUrl = "http://https://nonnoumenal-hyo-unorbed.ngrok-free.dev/builds/" + parser.commitHash; 
 
        try {
            // Prepare History Folder
            File buildHistoryDir = new File("build_history/" + parser.commitHash);
            if (!buildHistoryDir.exists()) {
                buildHistoryDir.mkdirs();
            }
            File logFile = new File(buildHistoryDir, "log.txt");

            // Clone
            boolean cloneSuccess = cloner.cloneBranch(parser.sshUrl, parser.branch);

            if (!cloneSuccess) {
                System.err.println("Failed to clone " + parser.branch);
                gitHubClient.notify(parser.repoName, parser.commitHash, false, logUrl);
                return;
            }

            // Print if cloneSuccess == true.
            System.out.println("Cloning branch: " + parser.branch + " to temporary directory: " + cloner.getTempDir());

            // Create temporary directory for every new request to prevent conflicts
            temporaryDir = cloner.getTempDir();

            //  Run the tests
            System.out.println("Running tests...");
            boolean success = MavenTestExecutor.run(temporaryDir.toFile(), logFile);
            System.out.println("Build finished. Status: " + (success ? "SUCCESS" : "FAILURE"));

            System.out.println("Updating Github status");
            gitHubClient.notify(parser.repoName, parser.commitHash, success, logUrl);


            emailNotifier.notify(parser.pusher, success, parser.commitHash, logUrl);
            

        } catch (Exception e) {
            e.printStackTrace();
            gitHubClient.notify(parser.repoName, parser.commitHash, false, logUrl);
            emailNotifier.notify(parser.pusher, false, parser.commitHash, logUrl);

        } finally {
            // Clean the disk
            if (temporaryDir != null) {
                cleaner.deleteDirectory(temporaryDir);
                System.out.println("Temporary files cleaned");
            }
        }
    }

    /**
     * Starts the CI server on port 8080. Handles webhooks and serves build history.
     *
     * @param args not used
     * @throws Exception if the server fails to start
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}
