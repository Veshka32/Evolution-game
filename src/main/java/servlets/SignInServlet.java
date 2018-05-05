package servlets;

import services.dataBaseService.DBService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/signIn")
public class SignInServlet extends HttpServlet {

    @Inject
    DBService dbService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        HttpSession session = req.getSession();

        try {
            if (dbService.isUserExist(login)) {
                Cookie cookie = new Cookie("player", login);
                resp.addCookie(cookie);
                session.setAttribute("player", login);
                req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
            } else{

                //if (((session.getAttribute("player"))).equals(login)) doGet(req,resp);
                req.setAttribute("signInError", "Sorry, invalid login");
                req.getRequestDispatcher("/index.jsp").forward(req, resp);}
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("signInError", "System error, try again");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);}
    }

}
//<% response.setIntHeader("Refresh", 5); %>
