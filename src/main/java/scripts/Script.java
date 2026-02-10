package scripts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs Maven (mvnw) in a project root. Give it the project root File and "compile" or "test"; it
 * builds the right command (Windows vs other OS) and runs it.
 */
public final class Script {

  private Script() {}

  /**
   * Runs Maven in the project root for the given action.
   *
   * @param projectRoot root directory of the project to CI (must contain mvnw / mvnw.cmd)
   * @param action "compile" (runs clean + compile) or "test" (runs test)
   * @return true if the process exited with 0, false otherwise
   */
  public static boolean run(File projectRoot, String action, File logFile)
      throws IOException, InterruptedException {
    List<String> cmd = buildCmd(action);
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

  private static List<String> buildCmd(String action) {
    String os = System.getProperty("os.name").toLowerCase();
    boolean windows = os.contains("win");

    String mvnw = windows ? "mvnw.cmd" : "./mvnw";
    List<String> goals = goalsFor(action);

    List<String> cmd = new ArrayList<>();
    cmd.add(mvnw);
    cmd.addAll(goals);
    return cmd;
  }

  private static List<String> goalsFor(String action) {
    switch (action) {
      case "compile":
        return List.of("clean", "compile");
      case "test":
        return List.of("test");
      default:
        throw new IllegalArgumentException(
            "action must be \"compile\" or \"test\", got: " + action);
    }
  }
}
