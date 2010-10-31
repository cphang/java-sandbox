package org.xin.restlet.impl;

import org.restlet.resource.ClientResource;
import org.xin.restlet.Contact;
import org.xin.restlet.ContactClient;
import org.xin.restlet.ContactsResource;

public class ContactClientImpl implements ContactClient {
  private final ClientResource client;
  private final ContactsResource contactsResource;

  public ContactClientImpl(String uri) {
    client = new ClientResource(uri);
    contactsResource = client.wrap(ContactsResource.class);

  }

  @Override
  public void send(Contact person) {
    client.post(person, ContactImpl.class);
  }

  @Override
  public Contact retrieve() {
    return contactsResource.retrieve();
  }

  // @Override
  // public Representation toJson() {
  // return contactsResource.toJson();
  // }
}
