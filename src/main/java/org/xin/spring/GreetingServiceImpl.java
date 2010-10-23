package org.xin.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreetingServiceImpl implements GreetingService {

  private final static Logger log = LoggerFactory.getLogger(GreetingServiceImpl.class);

  private String greeting;

  public GreetingServiceImpl() {
  }

  public GreetingServiceImpl(String greeting) {
    this.greeting = greeting;
  }

  public void sayGreeting() {
    log.info("say: " + greeting);
  }

  public void setGreeting(String greeting) {
    this.greeting = greeting;
  }
}
