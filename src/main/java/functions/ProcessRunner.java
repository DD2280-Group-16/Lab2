package functions;

import java.nio.file.Path;

/**
 * Runs a given command in a specific directory for running external
 * processes (Git or Maven)
 * 
 * @param dir Directory to run commmand in
 * @param command Command to be executed
 * @return Returns status of termination, 0 for normal termination
 */
public abstract class ProcessRunner {
    public abstract int run(Path dir, String... command) throws Exception;
}
