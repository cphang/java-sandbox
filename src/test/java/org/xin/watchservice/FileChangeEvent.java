package org.xin.watchservice;

import name.pachler.nio.file.Path;
import name.pachler.nio.file.WatchEvent.Kind;

public interface FileChangeEvent {

  Path getPath();

  Kind<?> kind();

  String absolutePath();
}
