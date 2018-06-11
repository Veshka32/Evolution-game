package services.websocketService;

import game.controller.GameManager;
import game.controller.Move;
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
    private GameManager gameManager;

    @Inject
    private SocketsHandler socketsHandler;

    @OnOpen
    public void open(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String player = (String) httpSession.getAttribute("player");
        Integer gameId = (Integer) httpSession.getAttribute("gameId");
        socketsHandler.addSession(session, player, httpSession, gameId);
        sendToAll(session,true);
    }

    private void sendToAll(Session session,boolean sendFullGame) {
        Integer gameId=socketsHandler.getGameId(session);
        Game game = gameManager.getGame(gameId);
        for (Session s : session.getOpenSessions()) {
            try {
                if ((socketsHandler.getGameId(s)).intValue() != gameId.intValue()) continue;
                String name = socketsHandler.getName(s);
                String message;
                if (sendFullGame) message = game.getFullJson(name);
                else message=game.getLightWeightJson(name);
                if (message != null) s.getBasicRemote().sendText(message); //null means error for one of the players
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        game.clearError();
        if (game.isEnd()) gameManager.remove(gameId);
    }

    @OnMessage
    public void handleMessage(Move message, Session session) {
        Integer gameId = socketsHandler.getGameId(session);
        if (message.getMove().equals("saveGame")) gameManager.save(gameId);
        gameManager.getGame(gameId).makeMove(message);
        sendToAll(session,false);
    }

    @OnClose
    public void close(Session session) {
        Move message=new Move(socketsHandler.getName(session),0,0,0,"Leave game",null," leave game");
        gameManager.getGame(socketsHandler.getGameId(session)).makeMove(message);
        sendToAll(session,false);
        socketsHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        //Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }


}