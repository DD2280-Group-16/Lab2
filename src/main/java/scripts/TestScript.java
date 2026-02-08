package scripts;

import java.util.List;

public class TestScript extends Script {

    public TestScript(String projectPath) {
        super(projectPath, buildCmd());
    }

    private static List<String> buildCmd() {
        boolean win = System.getProperty("os.name").toLowerCase().contains("win");
        if (win) {
            return List.of("cmd.exe", "/c", "mvnw.cmd", "test");
        } else {
            return List.of("./mvnw", "test");
        }
    }
}
