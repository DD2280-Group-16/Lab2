import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import scripts.MavenTestExecutor;

/**
 * Tests for {@link MavenTestExecutor}: run returns false when no mvnw, or true on a minimal passing project.
 */
public class MavenTestExecutorTest {

  /**
   * Test that run returns false when the project root does not exist or contains no mvnw.
   */
  @Test
  void runReturnsFalseWhenProjectRootHasNoMvnw(@TempDir Path tempDir)
      throws IOException, InterruptedException {
    File projectRoot = tempDir.toFile();

    boolean result = MavenTestExecutor.run(projectRoot, null);

    assertFalse(result);
  }

  /**
   * Test that run returns false when given an invalid project root and a log file.
   */
  @Test
  void runReturnsFalseWithLogFileWhenProjectRootInvalid(@TempDir Path tempDir)
      throws IOException, InterruptedException {
    File projectRoot = tempDir.toFile();
    File logFile = tempDir.resolve("log.txt").toFile();

    boolean result = MavenTestExecutor.run(projectRoot, logFile);

    assertFalse(result);
  }

  /**
   * Test that run returns false when project root is a non-existent path.
   */
  @Test
  void runReturnsFalseWhenProjectRootDoesNotExist() throws IOException, InterruptedException {
    File projectRoot = new File("nonexistent-path-12345");

    boolean result = MavenTestExecutor.run(projectRoot, null);

    assertFalse(result);
  }

  /**
   * Test that run returns true when given a minimal Maven project with a passing test.
   * Creates a fakeRepo: pom.xml, mvnw, and one test trivial test assertTrue(true).
   */
  @Test
  void runReturnsTrueWhenSampleProjectHasPassingTest(@TempDir Path tempDir)
      throws IOException, InterruptedException {
    Path root = Path.of(System.getProperty("user.dir"));

    //We get the mvnw and mvnw.cmd and copy them to a fake repo
    Path mvnw = root.resolve("mvnw");
    Path mvnwCmd = root.resolve("mvnw.cmd");
    Path dotMvn = root.resolve(".mvn").resolve("wrapper");
    Path fakeRepo = tempDir.resolve("sample-maven");
    Files.createDirectories(fakeRepo);

    Files.copy(mvnw, fakeRepo.resolve("mvnw"));
    if (Files.exists(mvnwCmd)) {
      Files.copy(mvnwCmd, fakeRepo.resolve("mvnw.cmd"));
    }
    Path fakeRepoDotMvn = fakeRepo.resolve(".mvn").resolve("wrapper");
    Files.createDirectories(fakeRepoDotMvn);
    for (Path p : Files.list(dotMvn).collect(Collectors.toList())) {
      Files.copy(p, fakeRepoDotMvn.resolve(p.getFileName()));
    }

    String pom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
      + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
      + "  xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
      + "  <modelVersion>4.0.0</modelVersion>\n"
      + "  <groupId>sample</groupId>\n"
      + "  <artifactId>sample</artifactId>\n"
      + "  <version>1.0</version>\n"
      + "  <properties>\n"
      + "    <maven.compiler.source>11</maven.compiler.source>\n"
      + "    <maven.compiler.target>11</maven.compiler.target>\n"
      + "  </properties>\n"
      + "  <dependencies>\n"
      + "    <dependency>\n"
      + "      <groupId>org.junit.jupiter</groupId>\n"
      + "      <artifactId>junit-jupiter</artifactId>\n"
      + "      <version>5.10.0</version>\n"
      + "      <scope>test</scope>\n"
      + "    </dependency>\n"
      + "  </dependencies>\n"
      + "</project>\n";
    Files.write(fakeRepo.resolve("pom.xml"), pom.getBytes(StandardCharsets.UTF_8));

    Path mainDir = fakeRepo.resolve("src/main/java/sample");
    Path testDir = fakeRepo.resolve("src/test/java/sample");
    Files.createDirectories(mainDir);
    Files.createDirectories(testDir);
    Files.write(mainDir.resolve("Main.java"), "package sample; public class Main { }\n".getBytes(StandardCharsets.UTF_8));
    String appTest = "package sample;"
        + "import org.junit.jupiter.api.Test;\n"
        + "import static org.junit.jupiter.api.Assertions.assertTrue;\n"
        + "public class AppTest { @Test void ok() { assertTrue(true); } }\n";
    Files.write(testDir.resolve("AppTest.java"), appTest.getBytes(StandardCharsets.UTF_8));

    boolean result = MavenTestExecutor.run(fakeRepo.toFile(), null);

    assertTrue(result, "Maven test on sample project should succeed");
  }
}
