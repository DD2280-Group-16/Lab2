package scripts;

import java.util.List;

public class CompileScript extends Script {

    public CompileScript(String projectPath) {
        super(projectPath, buildCmd());
    }

    private static List<String> buildCmd() {
        boolean win = System.getProperty("os.name").toLowerCase().contains("win");
        if (win) {
            return List.of("cmd.exe", "/c", "mvnw.cmd", "compile", "clean");
        } else {
            return List.of("./mvnw", "compile", "clean");
        }
    }
}
