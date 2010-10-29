package org.xin.watchservice;

import java.io.File;

import name.pachler.nio.file.Path;
import name.pachler.nio.file.WatchEvent.Kind;

public class FileChangeEventImpl implements FileChangeEvent {

  private final Kind<?> kind;
  private final Path path;
  private final Path watchDir;

  public FileChangeEventImpl(Kind<?> kind, Path watchDir, Path context) {
    this.kind = kind;
    this.watchDir = watchDir;
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

  @Override
  public String absolutePath() {
    return new StringBuilder(watchDir.toString()).append(File.separatorChar).append(
        path.toString()).toString();
  }

}
