package services.dataBaseService;

import game.controller.Game;
import org.junit.jupiter.api.Test;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.*;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {

    //@Test
//    void game() throws HeuristicMixedException, RollbackException, SystemException, NamingException, HeuristicRollbackException, NotSupportedException {
//        GameDAO gameDAO=new GameDAO();
//        Game game=new Game();
//        game.addPlayer("test");
//        game.addPlayer("pop");
//
//        //
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("local");
//        EntityManager em = entityManagerFactory.createEntityManager();
//        em.getTransaction().begin();
//        em.persist(game);
//        em.flush();
//
//        int id=gameDAO.save(game);
//        Game savedGame=gameDAO.load(id);
//        assert (game.getId()==savedGame.getId());
//        assert (savedGame.getPlayer("test")!=null);
//        assert (savedGame.getPlayer("pop")!=null);

    //}

}