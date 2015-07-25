package fred.saqua;

import java.util.Map;

/**
 * Created by fred on 7/25/15.
 */
public interface SimpleQueue {
  SimpleQueueMessage push(Object messageObject);

  SimpleQueueMessage push(Object messageObject, MessageHeaders headers);

  SimpleQueueMessage push(Object messageObject, Map<String, String> headers);

  SimpleQueueMessage pull();

  boolean isEmpty();

  void Ack();

  void NAck();
}
