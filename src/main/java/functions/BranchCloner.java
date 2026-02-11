package functions;

import java.nio.file.Files;
import java.nio.file.Path;

public class BranchCloner {
    
    private final ProcessRunner runner;
    private Path dir;
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

    public Path getTempDir() {
        return this.dir;
    }
}
