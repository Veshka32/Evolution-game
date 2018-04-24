package servlets;

import model.UserBase;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/signUp")
public class SignUpServlet extends HttpServlet {

    @Inject
    UserBase userBase;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/cabinet.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login=req.getParameter("login");
        HttpSession session=req.getSession();

        if (userBase.signUp(login,session)) {

            Cookie cookie=new Cookie("player",login);
            resp.addCookie(cookie);
            session.setAttribute("player",login);
            req.getRequestDispatcher("/views/cabinet.jsp").forward(req,resp);
        }
        else {
            req.setAttribute("signUpError","Sorry, this login is already in use.");
            req.getRequestDispatcher("/index.jsp").forward(req,resp);
        }
    }
    //<% response.setIntHeader("Refresh", 5); %>
}
