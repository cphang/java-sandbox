package org.xin.jackson;


public interface ContactSender {

  void send(Contact person);

  // Representation toJson();

  Contact retrieve();

}
