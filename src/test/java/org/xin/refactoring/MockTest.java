package org.xin.refactoring;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MockTest {

  private final static Logger log = LoggerFactory.getLogger(MockTest.class);

  @SuppressWarnings("unchecked")
  @Test
  public void foo() {
    List<String> mockedList = mock(List.class);

    mockedList.add("one");
    mockedList.clear();

    verify(mockedList).add("one");
    verify(mockedList).clear();
    log.info("test");
  }
}
