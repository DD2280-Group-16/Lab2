import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class PayloadParserTest {
    /**
     * Test that verifies that the parser grabs the correct information.
     * @throws Exception
     */
    @Test
    void testThatVerifiesThatTheParserParsesCorrect() throws Exception {
        String body =
            "{"
            + "  \"ref\": \"refs/heads/main\","
            + "  \"after\": \"test123\","
            + "  \"repository\": {"
            + "    \"ssh_url\": \"git@github.com:test/repo.git\","
            + "    \"full_name\": \"test/repo\""
            + "  },"
            + "  \"pusher\": {"
            + "    \"name\": \"test\","
            + "    \"email\": \"testMail\""
            + "  }"
            + "}";

        PayloadParser parser = new PayloadParser(body);


        assertEquals(parser.sshUrl, "git@github.com:test/repo.git");
        assertEquals(parser.repoName, "test/repo");
        assertEquals(parser.commitHash, "test123");
        assertEquals(parser.branch, "main");
        assertEquals(parser.pusher, "testMail");
    }

    /**
     * Test that verifies that the parser throws IllegalArgumentException when the body is missing "ref"
     * @throws Exception
     */
    @Test
    void testThatVerifiesThatTheParserThrowsExceptionOnNoRef() throws Exception {
        String body =
            "{"
            + "  \"after\": \"test123\","
            + "  \"repository\": {"
            + "    \"ssh_url\": \"git@github.com:test/repo.git\","
            + "    \"full_name\": \"test/repo\""
            + "  },"
            + "  \"pusher\": {"
            + "    \"name\": \"test\","
            + "    \"email\": \"testMail\""
            + "  }"
            + "}";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PayloadParser(body)
        );

        assert(exception.getMessage().equals("Invalid payload: missing 'ref' field"));
    }
}
