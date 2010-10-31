package org.xin.watchservice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xin.restlet.ContactConstants;
import org.xin.restlet.ContactService;
import org.xin.restlet.impl.ContactServiceImpl;
import org.xin.watchservice.impl.FileChangeListenerServiceImpl;

import java.io.IOException;

public class WatchServiceTest {

  private static final String SOURCE_PATH = "/Users/bender/tmp/source";
  private final ContactService contactService = new ContactServiceImpl(
      ContactConstants.CONTACT_SERVICE_PORT_NUMBER);

  private FileChangeListenerService fileChangeListernerService;

  @Before
  public void setUp() throws Exception {
    contactService.start();
    fileChangeListernerService = new FileChangeListenerServiceImpl(
        new ObserverImpl());
    fileChangeListernerService.add(SOURCE_PATH);
    fileChangeListernerService.start();
  }

  @After
  public void cleanUp() {
    fileChangeListernerService.stop();
  }

  private void addSources() throws IOException {
    fileChangeListernerService.add(SOURCE_PATH);
  }

  @Test
  public void shouldMonitorGivenFolder() throws Exception {
    whenAnewFileCreated();
    assertThatTheClientGetNotification();
  }

  private void assertThatTheClientGetNotification() throws Exception {
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
