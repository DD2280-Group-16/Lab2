import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import functions.BranchCloner;
import functions.ProcessRunner;

public class BranchClonerTest {
    /**
     * Test that controls if cloneBranch returns true on successful cloning
     * @throws Exception
     */
    @Test
    void cloneBranchReturnTrueWhenCloningSucceeds() throws Exception {

        ProcessRunner runner = mock(ProcessRunner.class);
        
        String[] expectedCmd = {"git", "clone", "-b", "main", "--single-branch", "https://example.com/repo.git", "."};
        doReturn(0).when(runner).run(any(Path.class), aryEq(expectedCmd));

        BranchCloner cloner = new BranchCloner(runner);

        boolean success = cloner.cloneBranch("https://example.com/repo.git", "main");

        assertTrue(success);
    }

    /**
     * Test that controls that cloneBranch uses the correct arguments when running the command and that it
     * only run them once.
     * @throws Exception
     */
    @Test
    void cloneBranchUsesTheCorrectArguments() throws Exception {
        ProcessRunner runner = mock(ProcessRunner.class);
        
        String[] expectedCmd = {"git", "clone", "-b", "main", "--single-branch", "https://example.com/repo.git", "."};
        doReturn(0).when(runner).run(any(Path.class), aryEq(expectedCmd));
        BranchCloner cloner = new BranchCloner(runner);
        cloner.cloneBranch("https://example.com/repo.git", "main");
        verify(runner, times(1)).run(any(Path.class), aryEq(expectedCmd));
    }

    /**
     * Test that controls that cloneBranch returns false on failed cloning.
     * @throws Exception
     */
    @Test
    void cloneBranchReturnFalseWhenCloningFails() throws Exception {

        ProcessRunner runner = mock(ProcessRunner.class);
        
        String[] expectedCmd = {"git", "clone", "-b", "main", "--single-branch", "https://example.com/repo.git", "."};
        doReturn(128).when(runner).run(any(Path.class), aryEq(expectedCmd));

        BranchCloner cloner = new BranchCloner(runner);

        boolean success = cloner.cloneBranch("https://example.com/repo.git", "main");

        assertFalse(success);
    }

    /**
     * Test that controls that cloneBranch creates a temporary directory for cloning.
     * @throws Exception
     */
    @Test
    void cloneBranchCreatesATempDir() throws Exception {

        ProcessRunner runner = mock(ProcessRunner.class);
        
        String[] expectedCmd = {"git", "clone", "-b", "main", "--single-branch", "https://example.com/repo.git", "."};
        doReturn(0).when(runner).run(any(Path.class), aryEq(expectedCmd));

        BranchCloner cloner = new BranchCloner(runner);

        cloner.cloneBranch("https://example.com/repo.git", "main");

        assertNotNull(cloner.getTempDir());
    }

    /**
     * Test that verify that cloneBranch returns false om empty arguments
     * @throws Exception
     */
    @Test
    void cloneBranchReturnsFalseOnEmptyArgument1() throws Exception {

        ProcessRunner runner = mock(ProcessRunner.class);

        BranchCloner cloner = new BranchCloner(runner);

        boolean success = cloner.cloneBranch("", "main");

        assertFalse(success);
    }

    /**
     * Test that verify that cloneBranch returns false om empty arguments
     * @throws Exception
     */
    @Test
    void cloneBranchReturnsFalseOnEmptyArgument2() throws Exception {

        ProcessRunner runner = mock(ProcessRunner.class);

        BranchCloner cloner = new BranchCloner(runner);

        boolean success = cloner.cloneBranch("https://example.com/repo.git", "");

        assertFalse(success);
    }
}
