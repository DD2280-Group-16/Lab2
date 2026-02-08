import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import scripts.CompileScript;
import scripts.ScriptResult;

/**
 * Tests CompileScript. Runs "mvnw compile" on the current project.
 * No loop: compile does not run the test phase.
 */
class CompileScriptTest {

    @Test
    void run_compilesCurrentProject_returnsSuccess() throws IOException, InterruptedException {
        String projectPath = System.getProperty("user.dir");
        CompileScript script = new CompileScript(projectPath);

        ScriptResult result = script.run();

        assertNotNull(result);
        assertNotNull(result.getOutput());
        assertTrue(result.isSuccess(), "compile should succeed: " + result.getOutput());
    }

    @Test
    void run_invalidPath_returnsFailure() throws IOException, InterruptedException {
        Path invalid = Files.createTempDirectory("ci-nonexistent");
        invalid.toFile().deleteOnExit();
        // Use a path that has no mvnw so process will fail
        CompileScript script = new CompileScript(invalid.toString());

        ScriptResult result = script.run();

        assertNotNull(result);
        assertFalse(result.isSuccess());
    }
}
