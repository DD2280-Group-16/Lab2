package functions;

import java.nio.file.Path;

/**
 * Class that creates a process that executes the given command in the given directory
 */
public class DefaultProcessRunner extends ProcessRunner {
    
    @Override
    public int run(Path dir, String... command) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(dir.toFile());
        pb.inheritIO();
        Process process = pb.start();
        return process.waitFor();
    }
}
