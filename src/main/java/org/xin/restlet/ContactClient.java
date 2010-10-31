package org.xin.restlet;


public interface ContactClient {

  void send(Contact person);

  // Representation toJson();

  Contact retrieve();

}
