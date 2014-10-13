/* WebCat
 * Copyright (C) 2013 Tuna Oezer, General AI
 * All rights reserved.
 */

package ai.general.web;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Endpoint for user service.
 */
@ServerEndpoint(value="/user/user_service.wamp",
                subprotocols="wamp")
public class UserServlet extends WampServlet {

  /**
   * Called when the connection is opened.
   *
   * @param session The WebSocket session associated with the connection.
   */
  @OnOpen
  public synchronized void onOpen(Session session) {
    if (session.getRequestParameterMap().get("username").size() == 0 ||
        session.getRequestParameterMap().get("session_id").size() == 0) {
      // Closing the session causes Tomcat to throw an exception if client sends a message.
      return;
    }
    String username = session.getRequestParameterMap().get("username").get(0);
    String session_id = session.getRequestParameterMap().get("session_id").get(0);
    User user = UserManager.getInstance().getUser(username);
    if (user == null || !user.isLoggedInWithSessionId(session_id)) {
      return;
    }
    open(session, null, user.getUsername(), UserUris.userHomePath(user.getUsername()));
  }

  /**
   * Called when the connection is closed.
   */
  @OnClose
  public synchronized void onClose() {
    close();
  }

  /**
   * Called when a text message is received.
   *
   * @param data The incoming text message.
   */
  @OnMessage
  public synchronized void onMessage(String message) {
    process(message);
  }

  /**
   * Called when an error has occurred.
   *
   * @param error Error details.
   */
  @OnError
  public synchronized void onError(Throwable error) {
    log.catching(Level.DEBUG, error);
  }

  private static Logger log = LogManager.getLogger();
}
