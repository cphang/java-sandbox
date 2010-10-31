package org.xin.jackson;

import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface ContactsResource {
  @Post
  void create(Contact contact);

  @Get
  Contact retrieve();

  // @Get("json")
  // public Representation toJson();

}
