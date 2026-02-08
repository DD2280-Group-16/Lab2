package scripts;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import scripts.ScriptResult;

public abstract class Script {
    protected final List<String> cmd;
    protected final File projectDir;

    public Script(String projectPath, List<String> cmd) {
        this.cmd = cmd;

        String userHome = System.getProperty("user.home");

        File p = new File(projectPath);
        this.projectDir = p.isAbsolute() ? p : new File(userHome, projectPath);
    }

    public ScriptResult run() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(projectDir);
        pb.redirectErrorStream(true);

        Process p = pb.start();

        String output;
        try (InputStream is = p.getInputStream()) {
            output = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        int exitCode = p.waitFor();
        return new ScriptResult(exitCode, output);
    }
}
