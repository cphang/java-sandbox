package org.xin.dropbox;

public class NotifierImpl implements Notifier {

  private boolean isMessageReceived;

  public boolean getMessage() {
    return isMessageReceived;
  }

  public void setMessage(boolean isMessageReceived) {
    this.isMessageReceived = isMessageReceived;
  }

}
