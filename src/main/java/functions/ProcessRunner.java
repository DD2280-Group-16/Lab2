package functions;

import java.nio.file.Path;


public abstract class ProcessRunner {
  /**
   * Runs a given command in a specific directory for running external
   * processes (Git or Maven)
   *
   * @param dir Directory to run commmand in
   * @param command Command to be executed
   * @return exit code (int) representing status of termination, 0 for normal termination
   * @throws Exception if the process fails to start or throws
   */
    public abstract int run(Path dir, String... command) throws Exception;
}
