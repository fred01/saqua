# saqua
Simple Android Queue ( Useful for Async )

## What is it
This is Android/Java library with simple Queue interface and it persistent implementation used build in Android SQLite. It help perform async exchange with remote servers in case of unstable network connection. 

## Naming
Originally `saqua` - a fancy alcohol drink from Endmond Hamilton's cosmic opera "The Star Kings". "A cursed good drink!" as main person say about it. So, why not.

## Example usage

Initializing 
```java
public class App extends Application {
  ...
  // Init DB Queue factory, must be performed before obtained first queue  
  SimpleDBQueueFactory.initFactory(getApplicationContext());
  ...
}
```

Produce messages
```java
public void storeDocument(Document docToStore) {
    SimpleDBQueue backupQueue = SimpleDBQueueFactory.getQueue("backup_documents");
    backupQueue.push(docToStore, ImmutableMap.of("collection", backupCollection));
    SimpleDBQueue docsQueue = SimpleDBQueueFactory.getQueue("documents");
    docsQueue.push(docToStore);    
}

```


Consume messages
```java
public class BackupDocumentsService extends AsyncTask {
  @Override
  protected Object doInBackground(Object[] params) {
    SimpleDBQueue queue = SimpleDBQueueFactory.getQueue("backup_documents");
    while (!queue.isEmpty()) {
      SimpleQueueMessage message = queue.pull();
      String backupCollection = message.getHeaders().get("backup_collection");
      if (backupCollection == null) {
        queue.Ack(); // wrong record, skip
      } else {
        objectToStore = message.getBody();
        if (RemoteService.storeBackupObject(backupCollection, objectToStore)) {
          queue.Ack();
        } else {
          queue.NAck();
        }
      }
    }
    return null;
  }
}

```
