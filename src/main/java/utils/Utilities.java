package utils;

import java.nio.file.Files;
import java.nio.file.Path;import java.nio.file.Files;


public class Utilities {
    /**
     * Function that finds the path to bash on different file structures.
     * Should work onLinux, mac and windows
     */
    public String findBash() throws Exception {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        if (!isWindows) {
            return "bash"; // Linux/macOS
        }

        // Usual windows paths, add more if needed
        String[] candidates = {
            "C:\\Program Files\\Git\\bin\\bash.exe",
            "C:\\Program Files (x86)\\Git\\bin\\bash.exe"
        };

        for (String path : candidates) {
            if (Files.exists(Path.of(path))) return path;
        }

        throw new IllegalStateException("Bash not found on Windows");
    }
}
