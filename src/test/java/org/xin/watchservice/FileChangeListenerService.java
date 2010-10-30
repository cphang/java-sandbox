package org.xin.watchservice;

import java.io.IOException;

import name.pachler.nio.file.ClosedWatchServiceException;

public interface FileChangeListenerService {

  void add(String sourcePath) throws IOException;

  void remove(String otherSourcePath);

  void start() throws ClosedWatchServiceException, InterruptedException,
      IOException;

  void stop();

}
