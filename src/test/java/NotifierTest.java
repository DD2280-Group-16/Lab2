import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class NotifierTest {

      @Test
  void emailIsTrue() {

    EmailNotifier notifier = new EmailNotifier();

    assertTrue(notifier.notify("INPUT EMAIL", false, null, null));
  }
    
}
