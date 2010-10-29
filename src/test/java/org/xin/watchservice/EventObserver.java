package org.xin.watchservice;

public interface EventObserver {

  void notify(FileChangeEvent event);

}
