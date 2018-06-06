package services.websocketService;

import game.controller.GameManager;
import game.controller.Move;
import game.controller.Game;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.transaction.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ApplicationScoped
@ServerEndpoint(value = "/views/socket", configurator = SocketConfigurator.class, decoders = {Decoder.class})
public class WebSocketServer {

    @Inject
    private GameManager gameManager;

    @Inject
    private SocketsHandler socketsHandler;

    @OnOpen
    public void open(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String player = (String) httpSession.getAttribute("player");
        Integer gameId = (Integer) httpSession.getAttribute("gameId");
        socketsHandler.addSession(session, player, httpSession, gameId);
        sendToAll(session, gameId);
    }

    private void sendToAll(Session session, Integer gameId) {
        Game game = gameManager.getGame(gameId);
        for (Session s : session.getOpenSessions()) {
            try {
                if ((socketsHandler.getGameId(s)).intValue() != gameId.intValue()) continue;
                String name = socketsHandler.getName(s);
                String message = game.convertToJsonString(name);
                if (message != null) s.getBasicRemote().sendText(message); //null means error for one of the players
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        game.clearError();
    }

    @OnMessage
    public void handleMessage(Move message, Session session) {
        Integer gameId = socketsHandler.getGameId(session);
        if (message.getMove().equals("Leave game")) {
            HttpSession http = socketsHandler.getHttpSession(session);
            http.removeAttribute("gameId");
            try {
                session.getBasicRemote().sendText(message.getMove());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gameManager.getGame(gameId).makeMove(message);
        try {
            gameManager.update(gameId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendToAll(session, gameId);
    }

    @OnClose
    public void close(Session session) {
        socketsHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        //Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }


}