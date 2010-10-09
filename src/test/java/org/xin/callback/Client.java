package org.xin.callback;

public interface Client {

  void sendRequest(Server server, Command notifyMe);

}
