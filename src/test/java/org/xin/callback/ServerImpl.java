package org.xin.callback;

public class ServerImpl implements Server {

  public boolean gotMessage() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      return false;
    }
    return true;
  }
}
