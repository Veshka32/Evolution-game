package servlets;

import model.Game;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //req.setAttribute("sessionId",req.getSession().getId());
        req.getRequestDispatcher("/views/game.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName=req.getParameter("userName");
        HttpSession session=req.getSession();
        Game game=(Game)getServletContext().getAttribute("game");
        game.addPlayer(session,userName);
        req.setAttribute("message","Welcome, "+userName);
        req.getRequestDispatcher("/views/game.jsp").forward(req,resp);
    }
    //<% response.setIntHeader("Refresh", 5); %>
}
