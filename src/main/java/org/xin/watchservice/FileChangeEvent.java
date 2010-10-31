package org.xin.watchservice;

import name.pachler.nio.file.Path;
import name.pachler.nio.file.WatchEvent.Kind;

public interface FileChangeEvent {

  Path fileName();

  Kind<?> kind();

  String absolutePath();
}