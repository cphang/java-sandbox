package org.xin.jackson;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Test;

public class SerializePojoToJson {

  private static final int CONTACT_SERVICE_PORT_NUMBER = 8082;
  private final ContactSender client = new ContactSenderImpl(
      "http://localhost:" + CONTACT_SERVICE_PORT_NUMBER + "/contactMe/contacts");
  private static final String NAME = "foo";
  private final Contact person = new ContactImpl(NAME, 19);

  private final ContactService contactService = new ContactServiceImpl(
      CONTACT_SERVICE_PORT_NUMBER);

  @After
  public void cleanUp() throws Exception {
    contactService.stop();
  }

  @SuppressWarnings("boxing")
  @Test
  public void shouldReturnJsonRepresentationOfPojo() throws Exception {
    givenContactService();
    // final Representation retrieve = client.retrieve();
    final Contact retrieve = client.retrieve();
    assertThat(retrieve.age(), CoreMatchers.is(21));

    // whenContactIsSendThroughPutRequestAsJsonToContactService();
    // assert
    // assertThatContactObjectIsCreatedInContactService();
  }

  private void givenContactService() throws Exception {
    contactService.start();
  }

  private void whenContactIsSendThroughPutRequestAsJsonToContactService() {
    client.send(person);
  }

  private void assertThatContactObjectIsCreatedInContactService() {
    final Contact actual = contactService.findByName(NAME);
    // assertThat(actual, CoreMatchers.is(person));
  }
}
