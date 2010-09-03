package org.xin.patterns.abstractfactory;

import org.junit.Test;

public class AbstractFactoryApplicationTest {

  @Test
  public void create() {
    new AbstractFactoryApplication(createOsSpecificFactory());
  }

  private GuiFactory createOsSpecificFactory() {
    return new OsxFactory();
  }
}
