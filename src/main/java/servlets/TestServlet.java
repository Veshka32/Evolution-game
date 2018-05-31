package servlets;

import game.controller.Game;
import services.dataBaseService.GameDAO;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/test")
public class TestServlet extends HttpServlet {
    @Inject
    GameDAO gameDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Game game=new Game();
            game.addPlayer("test");
            game.addPlayer("pop");

        int id= 0;
        try {
            id = gameDAO.save(game);
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
        Game savedGame=gameDAO.load(id);
        System.out.println (game.getId()==savedGame.getId());
        System.out.println (savedGame.getPlayer("test")!=null);
        System.out.println (savedGame.getPlayer("pop")!=null);

        }

}
