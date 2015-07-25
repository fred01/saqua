package fred.saqua;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Date;
import java.util.Map;

/**
 * Created by fred on 7/11/14.
 */
public class SimpleDBQueue implements SimpleQueue {

  private static String LOG_TAG = SimpleDBQueue.class.getName();
  private SQLiteDatabase db;
  private Gson gson = new Gson();

  protected SimpleDBQueue(SQLiteDatabase db) {
    this.db = db;
  }

  private void saveMessage(SimpleQueueMessage message) {
    ContentValues values = new ContentValues();
    values.put("id", message.getId());
    values.put("created_at", message.getCreatedAt().getTime());
    values.put("type", message.getType());
    values.put("headers", gson.toJson(message.getHeaders()));
    values.put("body", message.getBody());

    db.insert("queue",null, values);
  }

  private SimpleQueueMessage loadMessage(ContentValues row) {
    SimpleQueueMessage message = new SimpleQueueMessage();
    message.setId(row.getAsString("id"));
    message.setType(row.getAsString("type"));
    message.setCreatedAt(new Date(row.getAsLong("created_at")));
    message.setHeaders(gson.fromJson(row.getAsString("headers"), MessageHeaders.class));
    message.setBody(row.getAsString("body"));
    return message;
  }

  public SimpleQueueMessage push(Object messageObject) {
    return push(messageObject, null);
  }

  public SimpleQueueMessage push(Object messageObject, Map<String, String> headers) {
    return push(messageObject, MessageHeaders.fromMap(headers));
  }

  public SimpleQueueMessage push(Object messageObject, MessageHeaders headers) {
    SimpleQueueMessage message = new SimpleQueueMessage();
    message.setType(messageObject.getClass().getName());
    message.setHeaders(headers);
    message.setBody(gson.toJson(messageObject));
    message.setCreatedAt(new Date());
    saveMessage(message);
    return message;
  }

  public synchronized boolean isEmpty() {
    Log.i(LOG_TAG, "Test queue emptyness");
    boolean result = getOneRow("select 1 from queue") == null;
    Log.i(LOG_TAG, "Queue is " + (result ? "" : "not") + "  empty");
    return result;
  }

  private ContentValues getOneRow(String query) {
    return getOneRow(query, null);
  }

  private ContentValues getOneRow(String query, String[] args) {
    Cursor c = db.rawQuery(query, args);
    if (c.getCount() > 0) {
      c.moveToFirst();
      ContentValues row = new ContentValues();
      DatabaseUtils.cursorRowToContentValues(c, row);
      c.close();
      return row;
    } else {
      return null;
    }
  }


  public synchronized SimpleQueueMessage pull() {

    if (db.inTransaction()) {
      throw new IllegalStateException("Transaction in progress. Only one active transaction possible. Please, call Ack or NAck to end transaction");
    }

    db.beginTransaction();
    ContentValues row = getOneRow("select id, created_at, type, headers, body from queue order by created_at limit 1");
    if (row == null) {
      Log.i(LOG_TAG, "Actually queue empty, commit transaction");
      db.setTransactionSuccessful();
      db.endTransaction();
      return null;
    } else {
      SimpleQueueMessage message = loadMessage(row);
      Log.i(LOG_TAG, "Retrieved message from queue: " + message.getType() + "[" + message.getId() + "]");
      db.delete("queue", " id = ? ", new String[]{message.getId()});
      Log.i(LOG_TAG, "Deleted message in transaction. Waiting ack or nack. Database in transaction: " + db.inTransaction());
      return message;
    }
  }

  public void Ack() {
    Log.i(LOG_TAG, "Acknowledge called, commit transaction");
    Log.i(LOG_TAG, "Database in transaction (before) : " + db.inTransaction());
    db.setTransactionSuccessful();
    db.endTransaction();
    Log.i(LOG_TAG, "Database now in transaction (after) : " + db.inTransaction());
  }

  public void NAck() {
    Log.i(LOG_TAG, "Not Acknowledge called, rollback transaction");
    Log.i(LOG_TAG, "Database in transaction (before) : " + db.inTransaction());
    db.endTransaction();
    Log.i(LOG_TAG, "Database now in transaction (after) : " + db.inTransaction());
  }
}
