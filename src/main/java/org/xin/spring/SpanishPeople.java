package org.xin.spring;

public class SpanishPeople {

  private final GreetingService greetingService;

  public SpanishPeople(GreetingService greetingService) {
    this.greetingService = greetingService;
  }

  public void say() {
    greetingService.sayGreeting();
  }
}
