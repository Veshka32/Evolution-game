package services.dataBaseService;

import game.controller.Game;
import game.entities.Users;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
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

    public void remove(int gameId){
        Game game=em.find(Game.class,gameId);
        em.remove(game);
    }

    public List<Game> getSavedGames(String login){
        TypedQuery<Game> tq = em.createQuery("SELECT g from Game g join g.players p where p.name=?1", Game.class);
        tq.setParameter(1, login);
        return tq.getResultList();
    }

    public Game save(Game game){
        return em.merge(game);
    }

    public Game load(int gameId,String login){
        TypedQuery<Game> tq = em.createQuery("SELECT g FROM Game g join g.players p where g.id=?1 and p.name=?2", Game.class);
        tq.setParameter(1, gameId);
        tq.setParameter(2, login);
        Game game;
        try {game = tq.getSingleResult();} catch (NoResultException e){game= null;}
        return game;
    }

    public List<Users> getUsers(String login, int gameId){
        TypedQuery<Users> tq=em.createQuery("select u from Users u join Game g join g.players p where g.id=?1 and p.name=?2",Users.class);
        tq.setParameter(1,gameId);
        tq.setParameter(2,login);
        return tq.getResultList();
    }
}
