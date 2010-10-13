package org.xin.dropbox;

import java.io.IOException;

public interface DropboxService {

  void watch(String dIRECTORY_TO_WATCH, Notifier notifier);

  void start() throws IOException, InterruptedException;

}
