package services.dataBaseService;

import game.controller.Game;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class GameDAO {
    @PersistenceContext(type=PersistenceContextType.EXTENDED)
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

    public Game create(){
        Game game=new Game();
        em.persist(game);
        return game;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void commit() {
        // changes will be committed here because
        // there is an active transaction and
        // pending changes exist in managed entities
        // (Employee with ID=4 name was changed)
    }
}
