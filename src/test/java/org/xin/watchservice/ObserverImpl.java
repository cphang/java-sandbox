package org.xin.watchservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xin.restlet.Contact;
import org.xin.restlet.ContactClient;
import org.xin.restlet.ContactConstants;
import org.xin.restlet.impl.ContactClientImpl;

public class ObserverImpl implements Observer {

  private final static Logger log = LoggerFactory.getLogger(ObserverImpl.class);

  private final ContactClient client = new ContactClientImpl(
      ContactConstants.URI);

  @Override
  public void notify(FileChangeEvent event) {
    final Contact contact = client.retrieve();
    log.info("got notification on file event: " + event + " and then got: "
        + contact);
    System.out.println("got notification on file event: " + event
        + " and then got: " + contact);
  }

}
