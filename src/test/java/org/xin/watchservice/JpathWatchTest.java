package org.xin.watchservice;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import name.pachler.nio.file.ClosedWatchServiceException;
import name.pachler.nio.file.FileSystems;
import name.pachler.nio.file.Path;
import name.pachler.nio.file.Paths;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchEvent.Kind;
import name.pachler.nio.file.WatchKey;
import name.pachler.nio.file.WatchService;

public class JpathWatchTest {

  private final static Logger log = LoggerFactory.getLogger(JpathWatchTest.class);

  private static final String SOURCE_PATH = "/Users/bender/tmp/source";
  private final Path source = Paths.get(SOURCE_PATH);
  private final WatchService watchService = FileSystems.getDefault().newWatchService();

  @Test
  public void shouldMonitorForFileModification() throws Exception {
    registerAllEvents();
    startMonitoring();
  }

  private void registerAllEvents() throws IOException {
    source.register(watchService, StandardWatchEventKind.ENTRY_CREATE,
        StandardWatchEventKind.ENTRY_DELETE,
        StandardWatchEventKind.ENTRY_MODIFY, StandardWatchEventKind.OVERFLOW);
  }

  private void startMonitoring() throws ClosedWatchServiceException,
      InterruptedException {
    for (;;) {
      for (final WatchEvent<?> watchEvent : pollEvents()) {
        outputEvent(watchEvent);
      }
    }
  }

  private void outputEvent(WatchEvent<?> watchEvent) {
    final Kind<?> kind = watchEvent.kind();

    String message = null;
    if (kind.equals(StandardWatchEventKind.ENTRY_CREATE)) {
      message = getContext(watchEvent).toString() + " created";
    } else if (kind.equals((StandardWatchEventKind.ENTRY_DELETE))) {
      message = getContext(watchEvent).toString() + " deleted";
    } else if (kind.equals(StandardWatchEventKind.ENTRY_MODIFY)) {
      message = getContext(watchEvent).toString() + " modified";
    } else if (kind.equals((StandardWatchEventKind.OVERFLOW))) {
      message = "OVERFLOW: more changes happened than we could retreive";
    } else {
      throw new RuntimeException("Unknown type of event.");
    }
    log.info(new StringBuilder(message).toString());
  }

  private List<WatchEvent<?>> pollEvents() throws ClosedWatchServiceException,
      InterruptedException {
    final WatchKey signaledKey = watchService.take();
    final List<WatchEvent<?>> pollEvents = signaledKey.pollEvents();
    signaledKey.reset();
    return pollEvents;
  }

  private Path getContext(WatchEvent<?> watchEvent) {
    return (Path) watchEvent.context();
  }
}
