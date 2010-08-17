package org.xin.refactoring;

import org.xin.refactoring.App.Type;

public class FooCommand implements MyCommand {

  public String execute() {
    return Type.FOO + App.ME;
  }
}
