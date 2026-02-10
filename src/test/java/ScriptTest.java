import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import scripts.Script;


class ScriptTest {

    @Test
    void run_compileOnCurrentProject_returnsTrue() throws IOException, InterruptedException {
        File projectRoot = new File(System.getProperty("user.dir"));
        boolean ok = Script.run(projectRoot, "compile");
        assertTrue(ok, "compile should succeed on this project");
    }

    @Test
    void run_invalidProjectRoot_returnsFalse() throws IOException, InterruptedException {
        Path invalid = Files.createTempDirectory("ci-no-mvnw");
        invalid.toFile().deleteOnExit();
        boolean ok = Script.run(invalid.toFile(), "compile");
        assertFalse(ok);
    }

    @Test
    void run_unknownAction_throws() {
        File projectRoot = new File(System.getProperty("user.dir"));
        assertThrows(IllegalArgumentException.class, () -> Script.run(projectRoot, "package"));
    }
}
