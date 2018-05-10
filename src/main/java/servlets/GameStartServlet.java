package servlets;

import model.Game;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/start")
public class GameStartServlet extends HttpServlet {
    @Inject
    Game game;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String name=(String) session.getAttribute("player");

        if (game.containsPlayer(name)){req.setAttribute("playersList",game.getAllPlayers());
            resp.sendRedirect("views/socket.html");}

        else if (game.isFull()) {
            req.setAttribute("message", "Sorry,game is full");
            req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
        } else {
            game.addPlayer(name);
            req.setAttribute("playersList",game.getAllPlayers());
            resp.sendRedirect("views/socket.html");
        }
    }
}
