package services.dataBaseService;

import game.controller.Game;
import game.entities.Users;

import javax.enterprise.context.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RequestScoped
public class GameDAO {
    @PersistenceContext
    private EntityManager em; //because of JTA resource-type

    public GameDAO() {
    }

    public int save(Game game) throws SystemException, NotSupportedException, NamingException, HeuristicRollbackException, HeuristicMixedException, RollbackException {

        UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        transaction.begin();
        em.persist(game);
        em.flush();
        transaction.commit();
        return game.getId();
    }

    public Game load(int id) throws NoResultException{
        //Game game=em.find(Game.class,id);
        TypedQuery<Game> tq = em.createQuery("SELECT c FROM Game c where c.id=?1", Game.class);
        tq.setParameter(1, id);
        Game game = tq.getSingleResult();
        return game;
    }

//    private void test(){
//        Game game=new Game();
//        game.addPlayer("test");
//        game.addPlayer("pop");
//
//        int id= 0;
//        try {
//            id = gameDAO.save(game);
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
