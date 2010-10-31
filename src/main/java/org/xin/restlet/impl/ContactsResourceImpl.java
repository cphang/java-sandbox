package org.xin.restlet.impl;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xin.restlet.Contact;
import org.xin.restlet.ContactsResource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContactsResourceImpl extends ServerResource implements
    ContactsResource {

  private final static Logger log = LoggerFactory.getLogger(ContactsResourceImpl.class);

  private final Map<String, Contact> contactByName = new ConcurrentHashMap<String, Contact>(
      5);

  @Post
  @Override
  public void create(Contact contact) {
    if (contact == null) {
      setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
    } else {
      assert contactByName.size() == 0 : "new contact is not empty";
      contactByName.put(contact.name(), contact);
      assert contactByName.size() > 0 : "new contact is not created";

      setStatus(Status.SUCCESS_CREATED);
    }
  }

  @Get
  @Override
  public Contact retrieve() {
    return new ContactImpl("bar", 21);
  }

  // @Override
  // public Representation toJson() {
  // log.info("got");
  // final Map<String, String> map = new HashMap<String, String>();
  // map.put("foo", "bar");
  // return new JsonRepresentation(map);
  // }
}
