package servlets;

import game.controller.GameManager;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.transaction.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/create")
public class CreateGameServlet extends HttpServlet {
    @Inject
    private GameManager gameManager;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        String login=(String) session.getAttribute("player");
        int numberOfPlayers=Integer.valueOf(req.getParameter("number"));

        try {
            int gameId = gameManager.createGame(login,numberOfPlayers);
            session.setAttribute("gameId",gameId);
            resp.sendRedirect("views/socket.html");
        } catch (IllegalArgumentException e){
            req.setAttribute("createError","Wrong number of players");
            req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
        }
        catch (Exception e){
            req.setAttribute("createError","System error, try again.");
            req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
        }
    }


}
