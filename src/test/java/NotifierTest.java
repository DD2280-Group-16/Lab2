import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NotifierTest {

      @Test
  void emailIsTrue() {

    Notifier notifier = new Notifier();

    assertTrue(notifier.sendNotification("INPUT EMAIL", false, null, null));
  }
    
}
