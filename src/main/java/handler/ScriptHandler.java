package handler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import utils.Utilities;
public class ScriptHandler {
    public void runScript(String script, String branch) throws Exception {
        //String bash = "C:\\Program Files\\Git\\bin\\bash.exe";
        Utilities utils = new Utilities();
        String bash = utils.findBash();
        ProcessBuilder pb = new ProcessBuilder(bash, script, branch);

        pb.redirectErrorStream(true);

        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        int exitCode = process.waitFor();
        if(exitCode != 0) {
            throw new RuntimeException("Script failed with exit code: " + exitCode);
        }
    }
}
