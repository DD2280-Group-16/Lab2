package handler;

import java.nio.file.Path;
import java.nio.file.Paths;

public class HandlePullRequests {
    private final String branch;
    

    public HandlePullRequests(String branch) {
        this.branch = branch;
        
    }
    public void handleBuild() {
        ScriptHandler scriptHandler = new ScriptHandler();
        
        try {
            Path filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "handler", "git_build.sh");
            scriptHandler.runScript(filePath.toString(), this.branch);
        } catch (Exception e) {
            System.out.print(e);
        }
        

    }
}
