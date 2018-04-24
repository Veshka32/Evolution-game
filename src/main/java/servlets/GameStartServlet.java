package servlets;

import entities.Phase;
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

        if (game.isFull()){
            req.setAttribute("message", "Sorry,game is full");
            req.getRequestDispatcher("/views/cabinet.jsp").forward(req,resp);
        }
        else {
            HttpSession session=req.getSession();
            game.addPlayer((String) session.getAttribute("player"));
            if (game.isFull()){
                game.setStatus(Phase.EVOLUTION);}
            resp.sendRedirect("socket.html");
        }

    }
}
