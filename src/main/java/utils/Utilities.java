package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utilities {
    
    public String findBash() throws Exception {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        if (!isWindows) {
            return "bash"; // Linux/macOS
        }

        // Vanliga Git Bash paths
        String[] candidates = {
            "C:\\Program Files\\Git\\bin\\bash.exe",
            "C:\\Program Files (x86)\\Git\\bin\\bash.exe"
        };

        for (String path : candidates) {
            if (Files.exists(Path.of(path))) return path;
        }

        throw new IllegalStateException("Bash not found on Windows");
    }

    public Path findProjectRoot() throws Exception {
        Process p = new ProcessBuilder("git", "rev-parse", "--show-toplevel").start();
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(p.getInputStream()))) {
            String line = r.readLine();
            if (line == null) throw new IllegalStateException("git rev-parse returned nothing");

            line = line.trim(); 

            return Path.of(line);
        }
    }

}
