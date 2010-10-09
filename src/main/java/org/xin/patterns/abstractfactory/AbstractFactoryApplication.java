package org.xin.patterns.abstractfactory;

public class AbstractFactoryApplication {

  /**
   * @param factory
   */
  public AbstractFactoryApplication(GuiFactory factory) {
    Button button = factory.createButton();
    button.draw();
  }
}
