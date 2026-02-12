package functions;

import java.nio.file.Path;

/**
 * Class that creates a process that executes the given command in the given directory
 */
public class DefaultProcessRunner extends ProcessRunner {

    /**
     * Starts the command in the given directory and waits for it to finish.
     *
     * @param dir     the working directory
     * @param command the command and arguments
     * @return the process exit code (0 for success)
     * @throws Exception if the process fails to start or throws
     */
    @Override
    public int run(Path dir, String... command) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(dir.toFile());
        pb.inheritIO();
        Process process = pb.start();
        return process.waitFor();
    }
}
