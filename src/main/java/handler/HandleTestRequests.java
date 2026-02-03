package handler;

public class HandleTestRequests {
    private String branch;
    public HandleTestRequests(String branch) {
        this.branch = branch;
    }

    public void HandleTest() {
        ScriptHandler scriptHandler = new ScriptHandler();
        try {
            scriptHandler.runScript("./mvn_tests.sh", this.branch);
        } catch (Exception e) {
            System.out.print(e);
        }
    }
}