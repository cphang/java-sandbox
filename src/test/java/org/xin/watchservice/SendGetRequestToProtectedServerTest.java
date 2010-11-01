package org.xin.watchservice;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.restlet.representation.Representation;

public class SendGetRequestToProtectedServerTest {

  private final DownloadClient client = new DownloadClientImpl(DOWNLOAD_URI);

  @Test
  public void shouldGetListOfFilesIfAuthentifacted() throws Exception {
    client.loginWith(USER_NAME).passWord(PASSWORD);
    final Representation representation = client.get();
    assertThat(representation, CoreMatchers.notNullValue());
    System.out.println(representation.getText());
  }
}