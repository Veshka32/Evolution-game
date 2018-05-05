package servlets;

import services.dataBaseService.DBService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.websocket.Session;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/signOut")
public class SignOutServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.removeAttribute("player");
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }



}
//<% response.setIntHeader("Refresh", 5); %>

