package servlets;

import game.controller.Game;
import game.entities.Users;
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

@WebServlet(urlPatterns = "/create")
public class CreateGameServlet extends HttpServlet {

    @Inject
    private UsersDAO usersDAO;

    @Inject
    private GameDAO gameDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String name=(String) session.getAttribute("player");
        Users user=usersDAO.getUser(name);
        Game game=new Game();
        game.addPlayer(name,user);
        try {
            gameDAO.save(game);
        } catch (SystemException e) {
            e.printStackTrace();
        } catch (NotSupportedException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (HeuristicRollbackException e) {
            e.printStackTrace();
        } catch (HeuristicMixedException e) {
            e.printStackTrace();
        } catch (RollbackException e) {
            e.printStackTrace();
        }
    }

}
