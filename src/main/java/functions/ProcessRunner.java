package functions;

import java.nio.file.Path;


public abstract class ProcessRunner {
    public abstract int run(Path dir, String... command) throws Exception;
}
