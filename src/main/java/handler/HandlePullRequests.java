package handler;


public class HandlePullRequests {
    private final String branch;
    

    public HandlePullRequests(String branch) {
        this.branch = branch;
        
    }
    public void handleBuild() {
        ScriptHandler scriptHandler = new ScriptHandler();
        try {
            String script = "C:\\Users\\selin\\Documents\\CDATEMAST\\DD2480\\Lab2\\src\\main\\java\\handler\\git_build.sh";
            scriptHandler.runScript(script, this.branch);
        } catch (Exception e) {
            System.out.print(e);
        }
        

    }
}
