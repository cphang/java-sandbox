package org.xin.watchservice.impl;

import org.xin.watchservice.FileChangeEvent;

import java.io.File;
import java.io.Serializable;

import name.pachler.nio.file.Path;
import name.pachler.nio.file.WatchEvent.Kind;

public class FileChangeEventImpl implements FileChangeEvent, Serializable {

  private static final long serialVersionUID = 7933262081916693956L;
  private final Kind<?> kind;
  private final Path path;
  private final Path watchDir;

  public FileChangeEventImpl(Kind<?> kind, Path watchDir, Path context) {
    this.kind = kind;
    this.watchDir = watchDir;
    this.path = context;
  }

  @Override
  public Path fileName() {
    return path;
  }

  @Override
  public Kind<?> kind() {
    return kind;
  }

  @Override
  public String absolutePath() {
    return new StringBuilder(watchDir.toString()).append(File.separatorChar).append(
        path.toString()).toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return String.format("FileChangeEventImpl [kind=%s, path=%s, watchDir=%s]",
        kind, path, watchDir);
  }
}
