import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class EmailNotifierTest {

    @Mock
    EmailNotifier emailNotifier;

    @Test
    void testEmailSuccess() {
        EmailNotifier notifier = spy(new EmailNotifier());

        doReturn(true).when(notifier).performSend(any());

        String testEmail = "test@example.com";
        boolean result = notifier.notify(testEmail, true, "commit-123", "localhost:8080");

        assertTrue(result);

        verify(notifier, times(1)).performSend(any());
    }

    @Test
    void testInvalidEmailFailure() {
        EmailNotifier notifier = spy(new EmailNotifier());

        doThrow(IllegalArgumentException.class).when(notifier).performSend(any());

        assertThrows(IllegalArgumentException.class, () -> {
            notifier.notify("", true, "commit-123", "localhost:8080");
        });
    }

}