package org.xin.jackson;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class ContactServiceImpl implements ContactService {

  private final Component component = new Component();

  public ContactServiceImpl(int portNumber) {
    component.getClients().add(Protocol.FILE);
    component.getServers().add(Protocol.HTTP, portNumber);
    component.getDefaultHost().attach("/contactMe",
        new ContactResourceApplication());
  }

  @Override
  public Contact findByName(String name) {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void start() throws Exception {
    component.start();
  }

  @Override
  public void stop() throws Exception {
    component.stop();
  }

}