package org.xin.watchservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import name.pachler.nio.file.ClosedWatchServiceException;
import name.pachler.nio.file.FileSystems;
import name.pachler.nio.file.Path;
import name.pachler.nio.file.Paths;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchEvent.Kind;
import name.pachler.nio.file.WatchKey;
import name.pachler.nio.file.WatchService;
import name.pachler.nio.file.impl.PathWatchEvent;

public class FileChangeListenerServiceImpl implements FileChangeListenerService {

  private final static Logger log = LoggerFactory.getLogger(FileChangeListenerServiceImpl.class);

  private final Set<String> sources = new HashSet<String>();
  private final WatchService watchService = FileSystems.getDefault().newWatchService();

  private final FileChangeEventFactoryImpl factory = new FileChangeEventFactoryImpl();

  @Override
  public void add(String sourcePath) throws IOException {
    sources.add(sourcePath);
    regiterFileEvents(Paths.get(sourcePath));
  }

  private void regiterFileEvents(Path source) throws IOException {
    log.debug("registering " + source + " to the service");
    source.register(watchService, StandardWatchEventKind.ENTRY_CREATE,
        StandardWatchEventKind.ENTRY_DELETE,
        StandardWatchEventKind.ENTRY_MODIFY, StandardWatchEventKind.OVERFLOW);
  }

  @Override
  public void start() throws ClosedWatchServiceException, InterruptedException {
    log.debug("start monitoring all regitered folders");

    for (;;) {
      for (final WatchEvent<?> watchEvent : pollEvents()) {
        outputEvent(watchEvent);
      }
    }
  }

  private List<WatchEvent<?>> pollEvents() throws ClosedWatchServiceException,
      InterruptedException {

    final WatchKey signaledKey = watchService.take();
    final List<WatchEvent<?>> pollEvents = signaledKey.pollEvents();
    signaledKey.reset();

    for (final WatchEvent<?> watchEvent : pollEvents) {
      if (watchEvent instanceof PathWatchEvent) {
        final Kind<Path> kind = ((PathWatchEvent) watchEvent).kind(); // StandardWatchEventKind.ENTRY_CREATE
        log.debug(kind.type().getCanonicalName());

        final Path filePath = ((PathWatchEvent) watchEvent).context();
        log.debug(filePath.toString());
      }
    }

    return pollEvents;
  }

  private void outputEvent(WatchEvent<?> watchEvent) {
    final FileChangeEvent event = factory.create(watchEvent.kind(),
        getContext(watchEvent));

    fireEvent(event);
  }

  private void fireEvent(FileChangeEvent event) {
    final Path path = event.getPath();
    final Kind<?> kind = event.kind();

    String message = null;
    if (kind.equals(StandardWatchEventKind.ENTRY_CREATE)) {
      message = path.toString() + " created";
    } else if (kind.equals((StandardWatchEventKind.ENTRY_DELETE))) {
      message = path.toString() + " deleted";
    } else if (kind.equals(StandardWatchEventKind.ENTRY_MODIFY)) {
      message = path.toString() + " modified";
    } else if (kind.equals((StandardWatchEventKind.OVERFLOW))) {
      message = "OVERFLOW: more changes happened than we could retreive";
    } else {
      throw new RuntimeException("Unknown type of event.");
    }
    log.info(new StringBuilder(message).toString());

  }

  private Path getContext(WatchEvent<?> watchEvent) {
    return (Path) watchEvent.context();
  }

  @Override
  public void stop() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void remove(String sourcePath) {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }
}
