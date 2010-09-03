package org.xin.refactoring;

import java.io.File;

public class ListDirCommandImpl implements ListDirCommand {

  private File path;

  public Result execute() {
    if (path.isDirectory()) {
      return new ResultImpl(path.listFiles());
    }
    return new ResultImpl();
  }

  public ListDirCommand path(String string) {
    path = new File(string);
    return this;
  }

}
