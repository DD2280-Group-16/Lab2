import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import scripts.ScriptResult;

class ScriptResultTest {

    @Test
    void isSuccess_returnsTrue_whenExitCodeIsZero() {
        ScriptResult r = new ScriptResult(0, "output");
        assertTrue(r.isSuccess());
        assertEquals(0, r.getExitCode());
        assertEquals("output", r.getOutput());
    }

    @Test
    void isSuccess_returnsFalse_whenExitCodeNonZero() {
        ScriptResult r = new ScriptResult(1, "error");
        assertFalse(r.isSuccess());
        assertEquals(1, r.getExitCode());
        assertEquals("error", r.getOutput());
    }

    @Test
    void getOutput_returnsStoredOutput_includingNullAndEmpty() {
        ScriptResult withNull = new ScriptResult(0, null);
        assertNull(withNull.getOutput());
        ScriptResult withEmpty = new ScriptResult(1, "");
        assertEquals("", withEmpty.getOutput());
    }
}
