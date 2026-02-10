import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;

public class PayloadParserTest {
    /**
     * Test that verifies that the parser grabs the correct information.
     * Uses Mockito to mock ServletInputStream and HttpServletRequest
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
            + "  }"
            + "}";

        ByteArrayInputStream byteStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
        ServletInputStream servletInputStream = mock(ServletInputStream.class);

        /*
            JSONTokener calls InputStreamReader which uses read(byte[] buffer, int off, int len)
            Regular mocking resulted in zero bytes read.
        */
        when(servletInputStream.read(any(byte[].class), anyInt(), anyInt())).thenAnswer(invocation -> {
            byte[] buffer = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            return byteStream.read(buffer, off, len);
        });

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getInputStream()).thenReturn(servletInputStream);

        PayloadParser parser = new PayloadParser();

        parser.parse(request);

        assertEquals(parser.sshUrl, "git@github.com:test/repo.git");
        assertEquals(parser.repoName, "test/repo");
        assertEquals(parser.commitHash, "test123");
        assertEquals(parser.branch, "main");
    }
}
