package functions;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Clones a single branch of a Git repo into a temporary directory (removed after build).
 */
public class BranchCloner {
    
    private final ProcessRunner runner;
    private Path dir;

    /**
     * Builds a cloner that uses the given runner to run git commands.
     *
     * @param runner used to run the clone command
     */
    public BranchCloner(ProcessRunner runner) {
            this.runner = runner;
        }
        
    /**
     * Function that clones a single branch into a temporary directory which will be removed after testing/building are complete.
     * @param url The git url the branch is clones from
     * @param branch The branch to be cloned
     * @return Returns true if the cloning was successful, false otherwise
     */
    public boolean cloneBranch(String url, String branch) {
        if (url == null || url.isEmpty() || branch == null || branch.isEmpty()) {
            return false;
        }
        dir = null;
        try {
            dir = Files.createTempDirectory("branch-clone-");
            int exitCode = runner.run(dir ,"git", "clone", "-b", branch, "--single-branch", url, ".");

            return exitCode == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the path to the directory where the branch was cloned (null if not yet cloned or failed).
     *
     * @return the temp directory path, or null
     */
    public Path getTempDir() {
        return this.dir;
    }
}
