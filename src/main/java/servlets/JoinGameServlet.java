package servlets;

import game.controller.GameManager;

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
    private GameManager gameManager;
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
        String name = (String) session.getAttribute("player");
        Integer gameId = Integer.valueOf(req.getParameter("gameId"));

        try {
            if (!gameManager.isValidId(gameId)) {
                req.setAttribute("message", "Wrong game id");
                req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);

            } else if (gameManager.isCurrentGame(name, gameId)) {  //user already in game
                session.setAttribute("gameId", gameId);
                resp.sendRedirect("views/socket.html");

            } else {
                gameManager.joinPlayer(name, gameId); //new player
                session.setAttribute("gameId", gameId);
                resp.sendRedirect("views/socket.html");
            }
        } catch (Exception e) {
            req.setAttribute("message", "System error, try again.");
            req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
        }

    }
}