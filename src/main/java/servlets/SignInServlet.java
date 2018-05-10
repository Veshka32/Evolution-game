package servlets;

import services.dataBaseService.DBService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/signIn")
public class SignInServlet extends HttpServlet {

    @Inject
    DBService dbService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        HttpSession session = req.getSession();

        try {
            if (dbService.isUserValid(login, password)) {
                Cookie cookie = new Cookie("player", login);
                resp.addCookie(cookie);
                session.setAttribute("player", login);
                req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
            } else {
                sendLoginError(req, resp);
            }
        } catch (SQLException e) {
            sendSystemError(req, resp);
        } catch (InvalidKeySpecException e) {
            sendSystemError(req, resp);
        } catch (NoSuchAlgorithmException e) {
            sendSystemError(req, resp);
        }
    }

    void sendSystemError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("signUpError", "System error, try again");
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    void sendLoginError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("signInError", "Sorry,invalid login or password");
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
