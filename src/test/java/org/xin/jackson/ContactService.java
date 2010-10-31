package org.xin.jackson;

public interface ContactService {

  Contact findByName(String name);

  void start() throws Exception;

  void stop() throws Exception;

}
