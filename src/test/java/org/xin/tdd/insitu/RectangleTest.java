package org.xin.tdd.insitu;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RectangleTest {
  final MyRectangle r = new MyRectangleImp(4, 2, 0, 0);

  @Test
  public void shouldReturnAreaOfRectangle() {
    assertEquals("Area is not equals. ", 8, r.getArea());
  }

  @Test
  public void shouldReturnSumOfFoo() throws Exception {
    assertEquals("", 12, r.getSumFoo());
  }
}
