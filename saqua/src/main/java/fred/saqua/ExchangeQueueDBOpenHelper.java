package fred.saqua;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fred on 7/11/14.
 */
public class ExchangeQueueDBOpenHelper extends SQLiteOpenHelper {

  public ExchangeQueueDBOpenHelper(String databaseName, Context context) {
    super(context, databaseName, null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("create table queue ( " +
            " id text primary key," +
            " created_at integer," +
            " type text," +
            " headers text," +
            " body text" +
            " )");

  }

  @Override
  public SQLiteDatabase getWritableDatabase() {
    return super.getWritableDatabase();
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
