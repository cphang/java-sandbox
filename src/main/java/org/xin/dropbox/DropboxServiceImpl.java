package org.xin.dropbox;

import org.xin.jna.FileSystemWatcher;
import org.xin.jna.Notify;

import java.io.File;
import java.io.IOException;

public class DropboxServiceImpl implements DropboxService {

  final FileSystemWatcher watcher = FileSystemWatcher.getDefault();
  private Notifier notifier;

  public void watch(String dIRECTORY_TO_WATCH, Notifier notifier) {
    this.notifier = notifier;
    System.out.println("Adding listeners....");
    int count = 0;
    count += addWatch(new File(dIRECTORY_TO_WATCH), watcher);
    System.out.printf("Done. Added %d listeners\n", count);
  }

  private int addWatch(File root, Notify notify) {
    int count = 1;
    final File[] children = root.listFiles();
    if (children != null) {
      for (final File child : children) {
        if (child.isDirectory()) {
          count += addWatch(child, notify);
        }
      }
    }
    notify.addWatch(root.getAbsolutePath() + File.separatorChar);
    return count;
  }

  public void start() throws IOException, InterruptedException {
    watcher.start();
    System.out.println("Started");
    while (true) {
      final Notify.Event event = watcher.nextEvent();
      System.out.println("EVENT: " + event.getName() + " " + event.getKey()
          + " " + event.getKind());
      notifier.setMessage(true);
      return;
    }
  }

}
