package org.xin.callback;

public class ClientImpl implements Client {

  public void sendRequest(Server server, Command notifyMe) {
    server.runLongProcess();
    notifyMe.execute();
  }

}
