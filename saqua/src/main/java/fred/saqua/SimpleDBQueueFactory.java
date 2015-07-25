package fred.saqua;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fred on 12/7/14.
 */
public class SimpleDBQueueFactory {

  private static Context context;
  public static Map<String, SimpleDBQueue> queueMap = new HashMap<String, SimpleDBQueue>();

  public static void initFactory(Context context) {
    SimpleDBQueueFactory.context = context;
  }

  public static SimpleDBQueue getQueue(String queueName) {
    if (!queueMap.containsKey(queueName)) {
      SQLiteDatabase db = new ExchangeQueueDBOpenHelper("queue_"+queueName, context).getWritableDatabase();
      SimpleDBQueue queue = new SimpleDBQueue(db);
      queueMap.put(queueName, queue);
    }
    return queueMap.get(queueName);
  }
}
