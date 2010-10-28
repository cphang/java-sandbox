package org.xin.watchservice;

import name.pachler.nio.file.Path;
import name.pachler.nio.file.WatchEvent.Kind;

public class FileChangeEventImpl implements FileChangeEvent {

  private final Kind<?> kind;
  private final Path path;

  public FileChangeEventImpl(Kind<?> kind, Path context) {
    this.kind = kind;
    this.path = context;
  }

  @Override
  public Path getPath() {
    return path;
  }

  @Override
  public Kind<?> kind() {
    return kind;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "FileChangeEventImpl [kind=" + kind + ", path=" + path + "]";
  }

}
