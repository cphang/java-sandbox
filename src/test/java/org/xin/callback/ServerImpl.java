package org.xin.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerImpl implements Server {

  private final static Logger log = LoggerFactory.getLogger(ServerImpl.class);

  public boolean runLongProcess() {
    log.info("start running a long process");
    try {
      Thread.sleep(3000);
    } catch (final InterruptedException e) {
      return false;
    }
    log.info("finish");
    return true;
  }
}
