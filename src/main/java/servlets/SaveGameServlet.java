package servlets;

import game.controller.GameManager;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/save")
public class SaveGameServlet extends HttpServlet {

        @Inject
        private GameManager gameManager;

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            HttpSession session = req.getSession();
            Integer gameId = (Integer)(session.getAttribute("gameId"));
            gameManager.save(gameId);
            resp.sendRedirect("views/socket.html");
    }

}
