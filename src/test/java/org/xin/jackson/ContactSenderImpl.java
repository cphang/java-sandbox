package org.xin.jackson;

import org.restlet.resource.ClientResource;

public class ContactSenderImpl implements ContactSender {
  private final ClientResource client;
  private final ContactsResource contactsResource;

  public ContactSenderImpl(String uri) {
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
