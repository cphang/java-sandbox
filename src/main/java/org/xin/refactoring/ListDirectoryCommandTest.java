package org.xin.refactoring;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ListDirectoryCommandTest {

  @Test
  public void testShouldReturnListOfFiles() {
    ListDirCommand list = new ListDirCommandImpl();
    Result result = list.path("/Users/bender").execute();
    assertTrue(result.size() > 0);
    result.print();
  }
}
