package org.xin.refactoring;

import java.io.File;

public class ResultImpl implements Result {

  private final File[] listFiles;

  public ResultImpl(File[] listFiles) {
    this.listFiles = listFiles;
  }

  public ResultImpl() {
    this(new File[0]);
  }

  public void print() {
    for (File file : listFiles) {
      System.out.println(file.getName());
    }

  }

  public int size() {
    return listFiles.length;
  }

}
