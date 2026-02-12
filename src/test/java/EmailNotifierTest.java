import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link EmailNotifier}: notify succeeds with valid params, handles invalid email.
 */
public class EmailNotifierTest {

    @Mock
    EmailNotifier emailNotifier;

    /**
     * Tests that sending emails succeeds for correct parameters
     */
    @Test
    void testEmailSuccess() {
        Mailer mailer = MailerBuilder
                .withSMTPServer(
                        "smtp.gmail.com",
                        587,
                        "sender@example.com",
                        "123")
                .buildMailer();

        EmailNotifier notifier = spy(new EmailNotifier("sender@example.com", mailer));

        doReturn(true).when(notifier).performSend(any());

        String testEmail = "test@example.com";
        boolean result = notifier.notify(testEmail, true, "commit-123", "localhost:8080");

        assertTrue(result);

        verify(notifier, times(1)).performSend(any());
    }

    /**
     * Tests that sending emails throws exceptions for no email
     *
     */
    @Test
    void testInvalidEmailFailure() {
        Mailer mailer = MailerBuilder
                .withSMTPServer(
                        "smtp.gmail.com",
                        587,
                        "sender@example.com",
                        "123")
                .buildMailer();

        EmailNotifier notifier = spy(new EmailNotifier("sender@example.com", mailer));

        doThrow(IllegalArgumentException.class).when(notifier).performSend(any());

        assertThrows(IllegalArgumentException.class, () -> {
            notifier.notify("", true, "commit-123", "localhost:8080");
        });
    }

}
