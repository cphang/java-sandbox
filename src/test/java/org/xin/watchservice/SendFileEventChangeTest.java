package org.xin.watchservice;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

public class SendFileEventChangeTest {

  private ClientResource resource;

  @Before
  public void setUp() throws Exception {
    createMockServer();
    resource = new ClientResource("http://localhost:8082/foo");
  }

  private void createMockServer() throws Exception {
    new Server(Protocol.HTTP, 8082, FirstServerResource.class).start();
  }

  @Test
  public void shouldReturn2xx() throws Exception {
    whenFileEventSentAsJson();
    thenAssertThat200isReturn();
  }

  private void whenFileEventSentAsJson() {
    throw new UnsupportedOperationException("not-yet-implemented.");

  }

  private void thenAssertThat200isReturn() {
    final int code = resource.getResponse().getStatus().getCode();
    assertTrue("unsuccesful: " + code, code == 200);
  }

  private void whenPutSendMsgAsString() {
    resource.put(new StringRepresentation("msg"));
  }
}
