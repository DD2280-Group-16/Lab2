package scripts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs Maven (mvnw) in a project root. Give it the project root File and "test" it (compiles and runs the test).
 * Builds the right command (Windows vs other OS) and runs it.
 */
public final class MavenTestExecutor {

  private MavenTestExecutor() {}

  /**
   * Runs Maven in the project root to run test.
   *
   * @param projectRoot root directory of the project to CI (must contain mvnw / mvnw.cmd)
   * @return true if the process exited with 0, false otherwise
   */
  public static boolean run(File projectRoot, File logFile)
      throws IOException, InterruptedException {
    List<String> cmd = buildCmd();
    try {
      ProcessBuilder pb = new ProcessBuilder(cmd);
      pb.directory(projectRoot);
      // Save logs to file
      if (logFile != null) {
        pb.redirectOutput(logFile);
        pb.redirectErrorStream(true); // Catch errors in the same file
      } else {
        pb.inheritIO(); // Fallback to console if no file provided
      }
      Process process = pb.start();
      int exitCode = process.waitFor();
      return exitCode == 0;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Builds the commands to run tests
   * @return a list of string representing the commands and argument
   */
  private static List<String> buildCmd() {
    String os = System.getProperty("os.name").toLowerCase();
    boolean windows = os.contains("win");

    String mvnw = windows ? "mvnw.cmd" : "./mvnw";

    List<String> cmd = new ArrayList<>();
    cmd.add(mvnw);
    cmd.add("test");
    return cmd;
  }
}
