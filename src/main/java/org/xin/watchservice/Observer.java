package org.xin.watchservice;

public interface Observer {

  void notify(FileChangeEvent event);

}
