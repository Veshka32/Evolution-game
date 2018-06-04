package services.dataBaseService;

import game.controller.Game;
import game.entities.Users;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.*;
import javax.transaction.*;
import javax.transaction.RollbackException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class GameDAO {
    @PersistenceContext
    private EntityManager em; //because of JTA resource-type

    public Game create(Game game) throws SystemException, NotSupportedException, NamingException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        transaction.begin();
        game=em.merge(game);
        //em.flush();
        transaction.commit();
        return game;
    }

    public List<Game> getAllGames(){
        Query query = em.createQuery("SELECT c FROM Game c");
        List<Game> all = query.getResultList();
        return all;
    }

    public void update(Game game) throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException, NamingException {
        UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        transaction.begin();
        em.merge(game);
        transaction.commit();
    }
//    public Game load(int id) throws NoResultException{
//        //Game game=em.find(Game.class,id);
//        TypedQuery<Game> tq = em.createQuery("SELECT c FROM Game c where c.id=?1", Game.class);
//        tq.setParameter(1, id);
//        Game game = tq.getSingleResult();
//        return game;
//    }

//    private void test(){
//        Game game=new Game();
//        game.addPlayer("test");
//        game.addPlayer("pop");
//
//        int id= 0;
//        try {
//            id = gameDAO.create(game);
//        } catch (SystemException e) {
//            e.printStackTrace();
//        } catch (NotSupportedException e) {
//            e.printStackTrace();
//        } catch (NamingException e) {
//            e.printStackTrace();
//        } catch (HeuristicRollbackException e) {
//            e.printStackTrace();
//        } catch (HeuristicMixedException e) {
//            e.printStackTrace();
//        } catch (RollbackException e) {
//            e.printStackTrace();
//        }
//        Game savedGame=gameDAO.load(id);
//        assert (game.getId()==savedGame.getId());
//        assert (savedGame.getPlayer("test")!=null);
//        assert (savedGame.getPlayer("pop")!=null);
//
//    }
}
