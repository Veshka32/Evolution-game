package services.websocketService;

import game.entities.Move;
import game.controller.Game;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ApplicationScoped
@ServerEndpoint(value = "/views/socket", configurator = SocketConfigurator.class, decoders = {Decoder.class})
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
        sendToAll(session);
    }

    private void sendToAll(Session session) {
        for (Session s : session.getOpenSessions()) {
            try {
                String name=socketsHandler.getName(s);
                String message = game.convertToJsonString(name);
                if (message!=null) s.getBasicRemote().sendText(message); //null means error for one of the players
                System.out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        game.clearError();
    }

    @OnMessage
    public void handleMessage(Move message, Session session) {
        game.makeMove(message);
        sendToAll(session);
    }

    @OnClose
    public void close(Session session) {
//        game.deletePlayer(socketsHandler.getName(session));
//        sendToAll(session);
        socketsHandler.removeSession(session);

    }

    @OnError
    public void onError(Throwable error) {
        //Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }


}