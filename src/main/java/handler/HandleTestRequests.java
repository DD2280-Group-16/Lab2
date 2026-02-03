package handler;

public class HandleTestRequests {
    private String branch;
    public HandleTestRequests(String branch) {
        this.branch = branch;
    }

    public void handleTest() {
        ScriptHandler scriptHandler = new ScriptHandler();

        try {
            String root = System.getProperty("user.dir");
            String script = root + "\\src\\main\\java\\handler\\mvnw_tests.sh";
            scriptHandler.runScript(script, this.branch);
        } catch (Exception e) {
            System.out.print(e);
        }
    }
}