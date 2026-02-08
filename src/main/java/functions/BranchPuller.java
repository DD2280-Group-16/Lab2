package functions;

public class BranchPuller {
    /**
     * In the case of clone branch fails (if the repo already exists), the CI will checkout and pull the branch instead.
     * @param branch The branch that should be pulled
     * @return Returns true of the checkout and pull were successful
     */
    private boolean pullBranch(String branch) {
        try {
            
            System.out.println("Checkout to branch: " + branch + " and pulling.");
            /*
                Git checkout
            */
            ProcessBuilder pb1 = new ProcessBuilder("git", "checkout", branch);
            pb1.inheritIO();
            Process process1 = pb1.start();
            /*
                Git Pull
            */
            ProcessBuilder pb2 = new ProcessBuilder("git", "pull", "origin", branch);
            pb2.inheritIO();
            Process process2 = pb2.start();


            
            int exitCode1 = process1.waitFor();
            int exitCode2 = process1.waitFor();

            return (exitCode1 == 0 && exitCode2 == 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
