package fred.saqua;

import java.util.HashMap;
import java.util.Map;

/**
 * Marker class for typed json deserialize
 *
 * Created by fred on 7/25/15.
 */
public class MessageHeaders extends HashMap<String, String> {
  public MessageHeaders(Map<String, String> mapHeaders) {
    super(mapHeaders);
  }

  public static MessageHeaders fromMap(Map<String, String> mapHeaders) {
    return new MessageHeaders(mapHeaders);
  }
}
