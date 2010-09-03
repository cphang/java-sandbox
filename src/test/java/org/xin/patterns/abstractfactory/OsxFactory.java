package org.xin.patterns.abstractfactory;

public class OsxFactory implements GuiFactory {

  public Button createButton() {
    return new OsxButton();
  }

}
