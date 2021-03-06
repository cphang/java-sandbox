package org.xin.watchservice;

import name.pachler.nio.file.Path;
import name.pachler.nio.file.WatchEvent.Kind;

public interface FileChangeEventFactory {

  FileChangeEvent create(final Kind<?> kind, Path watchDir, Path context);

}
