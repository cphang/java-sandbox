package org.xin.watchservice;

import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstServerResource extends ServerResource {

  private final static Logger log = LoggerFactory.getLogger(FirstServerResource.class);

  @Put
  public void store(String msg) {
    log.info("get: " + msg);
  }

  @Override
  @Get
  public String toString() {
    return "hello, worlds";
  }
}
