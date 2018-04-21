package servlets;

import model.Game;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    @Inject
    Game game;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //req.setAttribute("sessionId",req.getSession().getId());
        req.getRequestDispatcher("/views/game.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("dopost");
        String userName=req.getParameter("userName");
        HttpSession session=req.getSession();
        game.addPlayer(session,userName);
        req.setAttribute("message","Welcome, "+userName);
        req.setAttribute("playersList",game.playersList());
        req.getRequestDispatcher("/views/game.jsp").forward(req,resp);
    }
    //<% response.setIntHeader("Refresh", 5); %>
}
