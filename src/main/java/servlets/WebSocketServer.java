package servlets;

import entities.Move;
import model.Game;
import services.Decoder;
import services.SocketConfigurator;
import services.SocketsHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ApplicationScoped
@ServerEndpoint(value = "/socket", configurator = SocketConfigurator.class, decoders = {Decoder.class})
public class WebSocketServer {

    @Inject
    private Game game;

    @Inject
    private SocketsHandler socketsHandler;

    @OnOpen
    public void open(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String player = (String) httpSession.getAttribute("player");
        socketsHandler.addSession(session, player);
        game.setMoves(player+" joined game");
        sendToAll(session);
    }

    @OnMessage
    public void handleMessage(Move message, Session session) {
        //Move mess = new Decoder().decode(message);
        game.makeMove(message);
        sendToAll(session);
    }

    @OnClose
    public void close(Session session) {
        socketsHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        //Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    private void sendToAll(Session session) {
        for (Session s : session.getOpenSessions()) {
            try {
                String message = game.convertToJsonString(socketsHandler.getName(s));
                s.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}