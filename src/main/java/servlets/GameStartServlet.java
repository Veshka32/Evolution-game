package servlets;

import game.controller.Game;
import game.controller.GameHandler;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/start")
public class GameStartServlet extends HttpServlet {
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
        Game game=gameHandler.getGame(gameId);
        if (game==null) {
            req.setAttribute("message", "Wrong game id");
            req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
        } else{
            game.addPlayer(name);
            session.setAttribute("gameId",gameId);
            resp.sendRedirect("views/socket.html");
        }
    }
}
