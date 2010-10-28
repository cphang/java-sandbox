package org.xin.watchservice;

import name.pachler.nio.file.Path;
import name.pachler.nio.file.WatchEvent.Kind;

public class FileChangeEventFactoryImpl implements FileChangeEventFactory {

  public FileChangeEvent create(final Kind<?> kind, Path context) {
    return new FileChangeEventImpl(kind, context);
  }
}
