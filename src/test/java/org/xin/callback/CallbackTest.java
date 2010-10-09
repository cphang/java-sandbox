package org.xin.callback;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CallbackTest {
  private final Client client = new ClientImpl();
  private final Server server = new ServerImpl();
  private boolean response;

  @Test
  public void shouldReturnOkAfterServerProcessTheRequest() {
    sendAsyncRequest();
    assertResponseIsTrue();
  }

  private void assertResponseIsTrue() {
    assertTrue("Should return true, but got false", response);
  }

  private void sendAsyncRequest() {
    client.sendRequest(server, createNotifier());
  }

  private Command createNotifier() {
    final Command notifyMe = new Command() {

      public void execute() {
        CallbackTest.this.response = true;
      }
    };
    return notifyMe;
  }
}
