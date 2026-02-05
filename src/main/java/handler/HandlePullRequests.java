package handler;

import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Class that handles CI pull requests.
 * Creates a path to the shell script that runs the mvn clean compile command and runs the script via ScriptHandler
 */
public class HandlePullRequests {
    private final String branch;
    
    public HandlePullRequests(String branch) {
        this.branch = branch;
        
    }
    public void handleBuild() {
        ScriptHandler scriptHandler = new ScriptHandler();
        
        try {
            /*
                Path builder to the script location, should work on different file structures.
            */
            Path filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "handler", "git_build.sh");
            scriptHandler.runScript(filePath.toString(), this.branch);
        } catch (Exception e) {
            System.out.print(e);
        }
        

    }
}
