package servlets;

import game.controller.Game;
import game.controller.GameHandler;
import game.entities.Users;
import services.dataBaseService.GameDAO;
import services.dataBaseService.UsersDAO;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.transaction.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@WebServlet(urlPatterns = "/create")
public class CreateGameServlet extends HttpServlet {
    @Inject
    GameHandler gameHandler;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String name=(String) session.getAttribute("player");
        int gameId=gameHandler.createGame(name);
        session.setAttribute("gameId",gameId);
        resp.sendRedirect("views/socket.html");
    }

}
