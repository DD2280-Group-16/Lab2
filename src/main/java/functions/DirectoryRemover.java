package functions;

import java.nio.file.Files;
import static java.nio.file.Files.walk;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * Removes a temporary directory
 */
public class DirectoryRemover {
    /**
     * Function that removes a given directory
     * @param dir Directory that should be removed
     * @return Returns true on successful removal, returns false otherwise.
     */
    public boolean deleteDirectory(Path dir) {
        if (dir == null || !Files.exists(dir)) {
            return false;
        }
        try {
            final boolean[] failed = {false};
            walk(dir).sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (Exception e) {
                    failed[0] = true;
                    e.printStackTrace();
                }
            });
            return !failed[0];
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
