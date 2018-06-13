package servlets;

import game.controller.GameManager;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/start")
public class JoinGameServlet extends HttpServlet {
    @Inject
    private GameManager gameManager;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String name = (String) session.getAttribute("player");
        Integer gameId = Integer.valueOf(req.getParameter("gameId"));

        try {

            gameManager.joinPlayer(name, gameId);
            session.setAttribute("gameId", gameId);
            resp.sendRedirect("views/socket.html");

        } catch (IllegalArgumentException e) {
            req.setAttribute("joinError", "Wrong game id");
            req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("joinError", "System error, try again.");
            req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
        }

    }
}