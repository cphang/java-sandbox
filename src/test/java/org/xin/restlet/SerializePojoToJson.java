package org.xin.restlet;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Test;
import org.xin.restlet.impl.ContactClientImpl;
import org.xin.restlet.impl.ContactImpl;
import org.xin.restlet.impl.ContactServiceImpl;

public class SerializePojoToJson {

  private final ContactClient client = new ContactClientImpl(
      ContactConstants.URI);
  private final Contact person = new ContactImpl(ContactConstants.NAME, 19);
  private final ContactService contactService = new ContactServiceImpl(
      ContactConstants.CONTACT_SERVICE_PORT_NUMBER);
  private Contact retrieve;

  @After
  public void cleanUp() throws Exception {
    contactService.stop();
  }

  @SuppressWarnings("boxing")
  @Test
  public void shouldReturnSerializedContactObject() throws Exception {
    givenContactService();
    whenRetrieved();
    assertThat(retrieve.age(), CoreMatchers.is(21));
  }

  private void givenContactService() throws Exception {
    contactService.start();
  }

  private void whenRetrieved() {
    retrieve = client.retrieve();
  }

  @Test
  public void shouldReturnJsonRepresentationOfPojo() throws Exception {
    givenContactService();
    whenContactIsSendThroughPutRequestAsJsonToContactService();
    assertThatContactObjectIsCreatedInContactService();
  }

  private void whenContactIsSendThroughPutRequestAsJsonToContactService() {
    client.send(person);
  }

  private void assertThatContactObjectIsCreatedInContactService() {
    final Contact actual = contactService.findByName(ContactConstants.NAME);
    // assertThat(actual, CoreMatchers.is(person));
  }
}
