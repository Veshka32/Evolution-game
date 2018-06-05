package servlets;

import game.controller.Game;
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

@WebServlet(urlPatterns = "/signUp")
public class SignUpServlet extends HttpServlet {

//    @Inject
//    DBService dbService;
    @Inject
    private UsersDAO usersDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login").toLowerCase();
        String password = req.getParameter("password");
        HttpSession session = req.getSession();

        if (login.isEmpty() || password.isEmpty()) {
            req.setAttribute("signUpError", "Put both login and password");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }

        try {
            if (usersDAO.addUser(login, password)) {
                Cookie cookie = new Cookie("player", login);
                resp.addCookie(cookie);
                session.setAttribute("player", login);
                req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
            } else {
                sendLoginError(req, resp);
            }
        }
        catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            sendError(req, resp);
            e.printStackTrace();
        }
    }

    private void sendError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("signUpError", "System error, try again");
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private void sendLoginError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("signUpError", "Sorry, this login is already in use.");
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
