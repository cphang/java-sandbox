package org.xin.refactoring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

  private final App app = new App();

  @Test
  public void shouldReturnFooMeIfFooSelected() {
    assertEquals(App.Type.FOO + App.ME, app.process(App.Type.FOO));
  }

  @Test
  public void shouldReturnFooMeIfBarSelected() {
    assertEquals(App.Type.BAR + App.ME, app.processUsingFactory(App.Type.BAR));
  }

  @Test
  public void shouldRetunFooMeIfFooIsSelected() throws InstantiationException,
      IllegalAccessException, ClassNotFoundException {
    assertEquals(App.Type.BAR + App.ME,
        app.processUsingReflection(App.Type.BAR));
  }
}