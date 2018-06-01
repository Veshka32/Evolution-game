package services.websocketService;

import game.controller.GameHandler;
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
    private GameHandler gameHandler;

    @Inject
    private SocketsHandler socketsHandler;

    @OnOpen
    public void open(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String player = (String) httpSession.getAttribute("player");
        Integer gameId=(Integer) httpSession.getAttribute("gameId");
        socketsHandler.addSession(session, player);
        socketsHandler.addGame(session,gameId);
        sendToAll(session,gameId);
    }

    private void sendToAll(Session session,Integer gameId) {
        Game game=gameHandler.getGame(gameId);
        for (Session s : session.getOpenSessions()) {
            try {
                if ((socketsHandler.getGameId(s)).intValue()!=gameId.intValue()) continue;
                String name=socketsHandler.getName(s);
                String message = game.convertToJsonString(name);
                if (message!=null) s.getBasicRemote().sendText(message); //null means error for one of the players
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        game.clearError();
    }

    @OnMessage
    public void handleMessage(Move message, Session session) {
        int gameId=socketsHandler.getGameId(session);
        gameHandler.getGame(gameId).makeMove(message);
        sendToAll(session,gameId);
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