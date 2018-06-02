//package servlets;
//
//import game.controller.Game;
//import game.controller.GameHandler;
//import services.dataBaseService.GameDAO;
//
//import javax.inject.Inject;
//import javax.naming.NamingException;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.transaction.*;
//import java.io.IOException;
//
//@WebServlet(urlPatterns = "/test")
//public class TestServlet extends HttpServlet {
//    @Inject
//    GameDAO gameDAO;
//    @Inject
//    GameHandler gameHandler;
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int gameId=gameHandler.createGame("pop");
//        gameHandler.joinPlayer("test",gameId);
//
//        int id=0;
//        try {
//            id = gameDAO.create(gameHandler.getGame(gameId));
//        } catch (SystemException e) {
//            e.printStackTrace();
//        } catch (NotSupportedException e) {
//            e.printStackTrace();
//        } catch (NamingException e) {
//            e.printStackTrace();
//        } catch (HeuristicRollbackException e) {
//            e.printStackTrace();
//        } catch (HeuristicMixedException e) {
//            e.printStackTrace();
//        } catch (RollbackException e) {
//            e.printStackTrace();
//        }
//
//        Game savedGame = gameDAO.load(id);
//        resp.getWriter().println(gameId == savedGame.getId());
//        resp.getWriter().println(savedGame.getPlayer("test") != null);
//        resp.getWriter().println(savedGame.getPlayer("pop") != null);
//
//    }
//
//}
