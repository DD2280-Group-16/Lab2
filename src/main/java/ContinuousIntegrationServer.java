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
        String body = request.getReader().lines().collect(Collectors.joining("\n"));
        Parser parser = new Parser(body);
        String branch = parser.getBranch();
        System.out.println(target);
        switch (target) {
            case "/test":
                
                HandleTestRequests tr = new HandleTestRequests(branch);
                tr.handleTest();
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
        // HandlePullRequests pr = new HandlePullRequests("feat/implement-payload-manager");
        // pr.handleBuild();
        // HandleTestRequests tr = new HandleTestRequests("feat/implement-payload-manager");
        // tr.handleTest();
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
        
    }
}