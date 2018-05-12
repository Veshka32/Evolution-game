package servlets;

import services.dataBaseService.DBService;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.transaction.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/signUp")
public class SignUpServlet extends HttpServlet {

    @Inject
    DBService dbService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        HttpSession session = req.getSession();

        if (login.isEmpty() || password.isEmpty()) {
            req.setAttribute("signUpError", "Put both login and password");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }

        try {
            if (dbService.addUser(login, password)) {
                Cookie cookie = new Cookie("player", login);
                resp.addCookie(cookie);
                session.setAttribute("player", login);
                req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
            } else {
                sendLoginError(req, resp);
            }
        }
//        catch (SQLException e) {
//            e.printStackTrace();
//            sendError(req, resp);
//        }
        catch (InvalidKeySpecException e) {
            sendError(req, resp);
        } catch (NoSuchAlgorithmException e) {
            sendError(req, resp);
        } catch (SystemException e) {
            sendError(req, resp);
            e.printStackTrace();
        } catch (NamingException e) {
            sendError(req, resp);
            e.printStackTrace();
        } catch (NotSupportedException e) {
            sendError(req, resp);
            e.printStackTrace();
        } catch (HeuristicMixedException e) {
            sendError(req, resp);
            e.printStackTrace();
        } catch (HeuristicRollbackException e) {
            sendError(req, resp);
            e.printStackTrace();
        } catch (RollbackException e) {
            sendError(req, resp);
            e.printStackTrace();
        }
    }

    void sendError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("signUpError", "System error, try again");
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    void sendLoginError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("signUpError", "Sorry, this login is already in use.");
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

}
//<% response.setIntHeader("Refresh", 5); %>
