package services.dataBaseService;

import game.controller.Game;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class GameDAO {
    @PersistenceContext
    private EntityManager em; //because of JTA resource-type

//    public Game create(Game game) throws SystemException, NotSupportedException, NamingException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
//        UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction"); //use if class is not ejb bean
//        transaction.begin();
//        game=em.merge(game);
//        //em.flush();
//        transaction.commit();
//        return game;
//    }

    public List<Game> getAllGames(){
        Query query = em.createQuery("SELECT c FROM Game c");
        List<Game> all = query.getResultList();
        return all;
    }

    public Game update(Game game){
        game=em.merge(game);
        return game;
    }

    public void remove(int gameId){
        Game game=em.find(Game.class,gameId);
        em.remove(game);
    }
}
