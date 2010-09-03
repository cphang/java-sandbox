package org.xin.refactoring;


public interface ListDirCommand {

  ListDirCommand path(String string);

  Result execute();

}
