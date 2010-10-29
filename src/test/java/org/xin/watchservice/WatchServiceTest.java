package org.xin.watchservice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import name.pachler.nio.file.ClosedWatchServiceException;

public class WatchServiceTest {

  private static final String SOURCE_PATH = "/Users/bender/tmp/source";

  private final FileChangeListenerService service = new FileChangeListenerServiceImpl();

  @Before
  public void setUp() throws IOException, ClosedWatchServiceException,
      InterruptedException {
    addSources();
    service.start();
  }

  @After
  public void cleanUp() {
    service.stop();
  }

  private void addSources() throws IOException {
    service.add(SOURCE_PATH);
  }

  @Test
  public void shouldMonitorGivenFolder() throws Exception {
    whenAnewFileCreated();
    assertThatTheClientGetNotification();
  }

  private void assertThatTheClientGetNotification() {
    throw new UnsupportedOperationException("not-yet-implemented.");

  }

  private void whenAnewFileCreated() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Test
  public void shouldThrownAnExceptionWhenTheFolderDoesNotExist()
      throws Exception {
    // service.add(OTHER_SOURCE_PATH);
  }
}
