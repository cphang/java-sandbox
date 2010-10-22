package org.xin.async;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.xin.callback.Client;
import org.xin.callback.ClientImpl;
import org.xin.callback.Command;
import org.xin.callback.Server;
import org.xin.callback.ServerImpl;

public class AsyncTest {

  private final Client client = new ClientImpl();
  private final Server server = new ServerImpl();
  private boolean response;

  @Test
  public void shouldReturnOkAfterServerProcessTheRequest() {
    sendAsyncRequest();
  }

  private void assertResponseIsTrue() {
    assertTrue("Should return true, but got false", response);
  }

  private void sendAsyncRequest() {
    client.sendRequest(server, createNotifier());
    foo();
    bar();
  }

  private void bar() {
    try {
      Thread.sleep(1000);
      System.out.println("2");
    } catch (final InterruptedException e) {
    }
  }

  private void foo() {
    try {
      Thread.sleep(1000);
      System.out.println("3");

    } catch (final InterruptedException e) {
    }
  }

  private Command createNotifier() {
    final Command notifyMe = new Command() {

      @Override
      public void execute() {
        System.out.println("1");
      }
    };
    return notifyMe;
  }
}
