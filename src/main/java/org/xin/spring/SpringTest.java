package org.xin.spring;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class SpringTest {

  private XmlBeanFactory factory;
  private GreetingService greetingService;

  @Before
  public void setUp() {
    createBeanFactoryFromFile();
    loadGreetingService();
  }

  @Test
  public void shouldFoo() throws Exception {
    new SpanishPeople(greetingService).say();
  }

  private void createBeanFactoryFromFile() {
    factory = new XmlBeanFactory(new ClassPathResource("hello.xml"));
  }

  private void loadGreetingService() {
    greetingService = (GreetingService) factory.getBean("greetingService");
  }
}