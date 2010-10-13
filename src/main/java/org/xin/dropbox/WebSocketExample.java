package org.xin.dropbox;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class WebSocketExample extends WebSocketServlet {

  private final static Logger log = LoggerFactory.getLogger(WebSocketExample.class);

  private final Set<WebSocket> _members = new CopyOnWriteArraySet<WebSocket>();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    getServletContext().getNamedDispatcher("default").forward(request, response);
  }

  @Override
  protected WebSocket doWebSocketConnect(HttpServletRequest request,
      String protocol) {
    return new ChatWebSocket();
  }

  class ChatWebSocket implements WebSocket {
    Outbound _outbound;

    public void onConnect(Outbound outbound) {
      _outbound = outbound;
      _members.add(this);
    }

    public void onMessage(byte frame, byte[] data, int offset, int length) {
    }

    public void onMessage(byte frame, String data) {
      for (final WebSocket member : _members) {
        try {
          ((ChatWebSocket) member)._outbound.sendMessage(frame, data);
        } catch (final IOException e) {
          log.warn("IOException: ", e);
        }
      }
    }

    public void onDisconnect() {
      _members.remove(this);
    }
  }
}
