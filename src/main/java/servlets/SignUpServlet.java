package servlets;

import entities.UserBase;
import model.Game;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/signUp")
public class SignUpServlet extends HttpServlet {

    @Inject
    UserBase userBase;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //req.setAttribute("sessionId",req.getSession().getId());
        req.getRequestDispatcher("/views/cabinet.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login=req.getParameter("login");
        HttpSession session=req.getSession();

        if (userBase.signUp(login,session)) {
            session.setAttribute("player",login);
            req.setAttribute("player",login);
            req.getRequestDispatcher("/views/cabinet.jsp").forward(req,resp);
        }
        else {
            req.setAttribute("signUpError","Sorry, this login is already in use.");
            req.getRequestDispatcher("/index.jsp").forward(req,resp);
        }
    }
    //<% response.setIntHeader("Refresh", 5); %>
}
