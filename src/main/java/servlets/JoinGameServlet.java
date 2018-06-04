package servlets;

import game.controller.Game;
import game.controller.GameHandler;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.transaction.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/start")
public class JoinGameServlet extends HttpServlet {
    @Inject
    private GameHandler gameHandler;
    //private Game game;

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        HttpSession session = req.getSession();
//        String name=(String) session.getAttribute("player");
//
//        if (game.getPlayer(name)!=null){req.setAttribute("playersList",game.getAllPlayers());
//        resp.sendRedirect("views/socket.html");}
//
//        else if (game.onProgress()) {
//            req.setAttribute("message", "Sorry,game is full");
//            req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
//        }
//        else {
//            game.addPlayer(name);
//            req.setAttribute("playersList",game.getAllPlayers());
//            resp.sendRedirect("views/socket.html");
//        }
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String name=(String) session.getAttribute("player");
        Integer gameId=Integer.valueOf(req.getParameter("gameId"));

        if (!gameHandler.isValidId(gameId)) {
            req.setAttribute("message", "Wrong game id");
            req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
        //user already in game
        } else if(gameHandler.isCurrentGame(name,gameId)){
            session.setAttribute("gameId",gameId);
            resp.sendRedirect("views/socket.html");
        //new player
        }else{
            try {
                gameHandler.joinPlayer(name,gameId);
            } catch (HeuristicMixedException e) {
                e.printStackTrace();
            } catch (NotSupportedException e) {
                e.printStackTrace();
            } catch (SystemException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            } catch (HeuristicRollbackException e) {
                e.printStackTrace();
            } catch (RollbackException e) {
                e.printStackTrace();
            }
            session.setAttribute("gameId",gameId);
            resp.sendRedirect("views/socket.html");
        }
    }
}
