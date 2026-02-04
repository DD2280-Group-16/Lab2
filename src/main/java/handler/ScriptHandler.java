package handler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import utils.Utilities;
public class ScriptHandler {
    public String runScript(String script, String branch) throws Exception {
        //String bash = "C:\\Program Files\\Git\\bin\\bash.exe";
        Utilities utils = new Utilities();
        String bash = utils.findBash();
        ProcessBuilder pb = new ProcessBuilder(bash, script, branch);
        StringBuilder sb = new StringBuilder();
        pb.redirectErrorStream(true);

        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
                //System.out.println(line);
            }
        }
        int exitCode = process.waitFor();
        if(exitCode != 0) {
            throw new RuntimeException("Script failed with exit code: " + exitCode);
        }
        String output = sb.toString();
        return output;
    }
}
