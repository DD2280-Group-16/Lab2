package functions;

public class BranchCloner {
    /**
     * Function that clones a single branch.
     * It is my understanding that this will return false if the repo already exists.
     * This can be handled by the BranchPuller class
     * @param url The git url the branch is clones from
     * @param branch The branch to be cloned
     * @return Returns true if the cloning was successful, false otherwise
     */
    private boolean cloneBranch(String url, String branch) {
        try {
            
            System.out.println("Cloning branch: " + branch);
            ProcessBuilder pb = new ProcessBuilder("git", "clone", "-b", branch, "--single-branch", url);

            pb.inheritIO();

            Process process = pb.start();
            int exitCode = process.waitFor();

            return exitCode == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
