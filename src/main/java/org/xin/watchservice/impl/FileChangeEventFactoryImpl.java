package org.xin.watchservice.impl;

import org.xin.watchservice.FileChangeEvent;
import org.xin.watchservice.FileChangeEventFactory;

import name.pachler.nio.file.Path;
import name.pachler.nio.file.WatchEvent.Kind;

public class FileChangeEventFactoryImpl implements FileChangeEventFactory {

  public FileChangeEvent create(final Kind<?> kind, Path watchDir, Path context) {
    return new FileChangeEventImpl(kind, watchDir, context);
  }

}
