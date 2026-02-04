import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import handler.HandlePullRequests;
import handler.HandleTestRequests;
import utils.Parser;

/** 
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/
public class ContinuousIntegrationServer extends AbstractHandler
{
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        //baseRequest.setHandled(true);
        /*
            Read the requests body
        */
        String body = request.getReader().lines().collect(Collectors.joining("\n"));
        /*
            Parsers the body for key information
        */
        Parser parser = new Parser(body);
        /*
            Gets the branch name for use in the handlers
        */
        String branch = parser.getBranch();
        /*
            Switches between different webhooks.
            Will fill up with more when we know that they are :)
        */
        switch (target) {
            case "/test":
                
                HandleTestRequests tr = new HandleTestRequests(branch);
                String notice = tr.handleTest();
                /*
                    Here we can handle the notification, for example:
                    String buildResult = tr.getBuildResult() // Not implemented :)
                    response.getWriter().println(buildResult);
                */
               response.getWriter().println(notice);
                break;
            case "/pull-request":;
                HandlePullRequests pr = new HandlePullRequests(branch);
                pr.handleBuild();
                break;
            default:
                throw new AssertionError();
        }
        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code

        response.getWriter().println("CI job done");
    }
 
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
    }
}