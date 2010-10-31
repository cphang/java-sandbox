package org.xin.watchservice;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
  public void start() throws ClosedWatchServiceException, InterruptedException,
      IOException {
    log.debug("start monitoring all regitered folders");
    for (;;) {
      pollEvents();
    }
  }

  private void pollEvents() throws ClosedWatchServiceException,
      InterruptedException, IOException {
    final WatchKey signaledKey = watchService.take();
    notifyListeners(signaledKey, signaledKey.pollEvents());
    signaledKey.reset();
  }

  private void notifyListeners(WatchKey signaledKey, List<WatchEvent<?>> events)
      throws IOException {
    for (final WatchEvent<?> watchEvent : events) {
      fireEvent(createEvent(watchEvent, signaledKey));
    }
  }

  private FileChangeEvent createEvent(WatchEvent<?> watchEvent,
      WatchKey signaledKey) {
    return factory.create(watchEvent.kind(), sourceByKey.get(signaledKey),
        getContext(watchEvent));
  }

  private void fireEvent(FileChangeEvent event) throws IOException {
    String message = null;
    if (event.kind().equals(StandardWatchEventKind.ENTRY_CREATE)) {
      message = event.absolutePath() + " created";
    } else if (event.kind().equals((StandardWatchEventKind.ENTRY_DELETE))) {
      message = event.absolutePath() + " deleted";
    } else if (event.kind().equals(StandardWatchEventKind.ENTRY_MODIFY)) {
      message = event.absolutePath() + " modified";
      log.info(new StringBuilder(message).toString());
      copyFile(event);
      sendMessage(event);
    } else if (event.kind().equals((StandardWatchEventKind.OVERFLOW))) {
      message = "OVERFLOW: more changes happened than we could retreive";
    } else {
      log.warn("unknown event type: " + event.kind());
    }
    log.info(new StringBuilder(message).toString());

  }

  private void sendMessage(FileChangeEvent event) {
    // TODO http-clint.put(address, event.asJSON);
  }

  final boolean running = true;

  private void copyFile(FileChangeEvent event) throws IOException {
    final String src = event.absolutePath();
    final String to = "/Users/bender/tmp/" + event.getPath().toString()
        + ".copy";
    final InputStream in = new URL("file:///" + src).openStream();
    IOUtils.copy(in, new FileOutputStream(to));
    log.info("copy finished");
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
