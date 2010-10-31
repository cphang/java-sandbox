package org.xin.jackson;

import com.google.common.base.Preconditions;

import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContactResourceImpl extends ServerResource implements
    ContactResource {

  private final Map<String, Contact> contactByName = new ConcurrentHashMap<String, Contact>(
      5);

  public Contact findByName(String name) {
    return contactByName.get(name);
  }

  @Put
  public void store(Contact contact) {
    Preconditions.checkNotNull(contactByName, "contactByName can not be null");
    Preconditions.checkNotNull(contact, "contact should not be null");

    assert contactByName.size() == 0 : "new contact is not empty";
    contactByName.put(contact.name(), contact);
    assert contactByName.size() > 0 : "new contact is not created";
  }

  @Override
  public Contact retrieve() {
    throw new UnsupportedOperationException("not-yet-implemented.");

  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("not-yet-implemented.");

  }
}
