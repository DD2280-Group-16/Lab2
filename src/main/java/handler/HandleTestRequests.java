package handler;

import java.nio.file.Path;
import java.nio.file.Paths;

public class HandleTestRequests {
    private String branch;
    public HandleTestRequests(String branch) {
        this.branch = branch;
    }

    public void handleTest() {
        ScriptHandler scriptHandler = new ScriptHandler();

        try {
            Path filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "handler", "mvnw_tests.sh");
            scriptHandler.runScript(filePath.toString(), this.branch);
        } catch (Exception e) {
            System.out.print(e);
            // return e.toString();
        }
    }
}