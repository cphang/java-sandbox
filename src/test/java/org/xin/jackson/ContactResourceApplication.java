package org.xin.jackson;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class ContactResourceApplication extends Application {

  @Override
  public Restlet createInboundRoot() {
    final Router router = new Router(getContext());
    router.attach("/contacts", ContactsResourceImpl.class);
    return router;
  }
}
