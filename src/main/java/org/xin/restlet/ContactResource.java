package org.xin.restlet;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface ContactResource {
  @Get
  Contact retrieve();

  @Put
  void store(Contact contact);

  @Delete
  void remove();
}
