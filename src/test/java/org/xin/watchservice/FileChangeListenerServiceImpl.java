package org.xin.watchservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import name.pachler.nio.file.ClosedWatchServiceException;
import name.pachler.nio.file.FileSystems;
import name.pachler.nio.file.Path;
import name.pachler.nio.file.Paths;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchKey;
import name.pachler.nio.file.WatchService;

public class FileChangeListenerServiceImpl implements FileChangeListenerService {

  private final static Logger log = LoggerFactory.getLogger(FileChangeListenerServiceImpl.class);

  private final Set<String> sources = new HashSet<String>();
  private final WatchService watchService = FileSystems.getDefault().newWatchService();

  private final FileChangeEventFactoryImpl factory = new FileChangeEventFactoryImpl();

  private final Map<WatchKey, Path> sourceByKey = new HashMap<WatchKey, Path>();

  @Override
  public void add(String sourcePath) throws IOException {
    sources.add(sourcePath);
    registerFileEvents(Paths.get(sourcePath));
  }

  private void registerFileEvents(Path source) throws IOException {
    log.debug("registering " + source + " to the service");
    final WatchKey key = source.register(watchService,
        StandardWatchEventKind.ENTRY_CREATE,
        StandardWatchEventKind.ENTRY_DELETE,
        StandardWatchEventKind.ENTRY_MODIFY, StandardWatchEventKind.OVERFLOW);
    sourceByKey.put(key, source);
  }

  @Override
  public void start() throws ClosedWatchServiceException, InterruptedException {
    log.debug("start monitoring all regitered folders");

    for (;;) {
      pollEvents();
    }
  }

  private List<WatchEvent<?>> pollEvents() throws ClosedWatchServiceException,
      InterruptedException {

    final WatchKey signaledKey = watchService.take();
    final Path watchDir = sourceByKey.get(signaledKey);

    final List<WatchEvent<?>> pollEvents = signaledKey.pollEvents();
    for (final WatchEvent<?> watchEvent : pollEvents) {
      final FileChangeEvent event = factory.create(watchEvent.kind(), watchDir,
          getContext(watchEvent));
      fireEvent(event);
    }
    signaledKey.reset();

    return pollEvents;
  }

  private Set<EventObserver> eventObservers;

  private void fireEvent(FileChangeEvent event) {
    // for (final EventObserver observer : eventObservers) {
    // observer.notify(event);
    // }

    String message = null;
    if (event.kind().equals(StandardWatchEventKind.ENTRY_CREATE)) {
      message = event.absolutePath() + " created";
      readFile(event);
    } else if (event.kind().equals((StandardWatchEventKind.ENTRY_DELETE))) {
      message = event.absolutePath() + " deleted";
    } else if (event.kind().equals(StandardWatchEventKind.ENTRY_MODIFY)) {
      message = event.absolutePath() + " modified";
    } else if (event.kind().equals((StandardWatchEventKind.OVERFLOW))) {
      message = "OVERFLOW: more changes happened than we could retreive";
    } else {
      throw new RuntimeException("Unknown type of event.");
    }
    log.info(new StringBuilder(message).toString());

  }

  final boolean running = true;

  private void readFile(FileChangeEvent event) {
    InputStream in = null;
    OutputStream out = null;
    try {
      in = new FileInputStream(event.absolutePath());
      out = new FileOutputStream("/Users/bender/tmp/"
          + event.getPath().toString() + ".copy");
      int c;

      while ((c = in.read()) != -1) {
        out.write(c);
      }

    } catch (final FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (final IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      if (out != null) {
        try {
          out.close();
        } catch (final IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
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
