package handler;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import utils.Utilities;


/**
 * Class that handles script running using shell scripts in BASH.
 * Uses ProcessBuilder to create a new process running the given script.
 * Can be used to grab the output to be sent to the Notify Handler.
 */
public class ScriptHandler {

    public void runScript(String script, String branch) throws Exception {
        Utilities utils = new Utilities();

        String bash = utils.findBash();

        ProcessBuilder pb = new ProcessBuilder(bash, script, branch);
        pb.redirectErrorStream(true);

        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            /*
                Prints the output for now.
            */
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        int exitCode = process.waitFor();
        if(exitCode != 0) {
            throw new RuntimeException("Script failed with exit code: " + exitCode);
        }
    }
    
}
