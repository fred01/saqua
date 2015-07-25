package fred.saqua;

import java.util.Date;
import java.util.UUID;

/**
 * Created by fred on 7/11/14.
 */
public class SimpleQueueMessage {

  private String id;
  private String type;
  private MessageHeaders headers;
  private String body;
  private Date createdAt;

  public SimpleQueueMessage() {
    this.id = UUID.randomUUID().toString();
    this.createdAt = new Date();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public MessageHeaders getHeaders() {
    return headers;
  }

  public void setHeaders(MessageHeaders headers) {
    this.headers = headers;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
}
