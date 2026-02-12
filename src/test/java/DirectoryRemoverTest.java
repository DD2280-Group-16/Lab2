import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

import functions.DirectoryRemover;

/**
 * Tests for {@link DirectoryRemover}: deleteDirectory removes a dir and its contents, returns false when dir does not exist.
 */
public class DirectoryRemoverTest {
    /**
     * Test that controls that deleteDirectory removes the given directory.
     * @throws Exception
     */
    @Test
    void deleteDirectoryRemovesAllFilesAndDirectories() throws Exception {
        Path tempDir = Files.createTempDirectory("test-dir");
        Path file1 = Files.createFile(tempDir.resolve("file1.txt"));
        Path subDir = Files.createDirectory(tempDir.resolve("sub"));
        Path file2 = Files.createFile(subDir.resolve("file2.txt"));

        DirectoryRemover remover = new DirectoryRemover();
        remover.deleteDirectory(tempDir);

        assertFalse(Files.exists(tempDir));
    }

    /**
     * Test that controls that deleteDirectory returns false if the given directory doesnt exist.
     * @throws Exception
     */
    @Test
    void deleteDirectoryReturnsFalseWhenDirectoryDoesNotExist() {
        DirectoryRemover remover = new DirectoryRemover();

        Path nonExistingDir = Path.of("no-dir");

        boolean result = remover.deleteDirectory(nonExistingDir);

        assertFalse(result);
    }
}
