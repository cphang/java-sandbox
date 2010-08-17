package org.xin.refactoring;

import org.xin.refactoring.App.Type;

public class BarCommand implements MyCommand {

  public String execute() {
    return Type.BAR + App.ME;
  }

}
