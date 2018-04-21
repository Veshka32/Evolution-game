package servlets;

import model.Game;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/start")
public class GameStartServlet extends HttpServlet {
    @Inject
    Game game;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (game.isFull()){
            req.setAttribute("message", "Sorry,game is full");
            req.getRequestDispatcher("/views/game.jsp").forward(req,resp);
        }
        else {
            resp.sendRedirect("/socket.html");
        }

    }
}