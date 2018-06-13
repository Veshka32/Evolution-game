package servlets;

import game.controller.GameManager;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.transaction.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/create")
public class CreateGameServlet extends HttpServlet {
    @Inject
    private GameManager gameManager;

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
//        HttpSession session = req.getSession();
//        String name=(String) session.getAttribute("player");
//        Integer gameId;
//        try {
//            gameId = gameManager.createGame(name,2);
//            session.setAttribute("gameId",gameId);
//            resp.sendRedirect("views/socket.html");
//        } catch (Exception e){
//            req.setAttribute("createError","System error, try again.");
//            req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
//        }
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        String login=(String) session.getAttribute("player");
        int number=Integer.valueOf(req.getParameter("number"));
        Integer gameId;
        try {
            gameId = gameManager.createGame(login,number);
            session.setAttribute("gameId",gameId);
            resp.sendRedirect("views/socket.html");
        } catch (Exception e){
            req.setAttribute("createError","System error, try again.");
            req.getRequestDispatcher("/views/cabinet.jsp").forward(req, resp);
        }
    }


}
