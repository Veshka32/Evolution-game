package services.dataBaseService;

import game.entities.Game;
import game.entities.Users;

import javax.ejb.Stateless;
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

    public void remove(Integer gameId) throws IllegalArgumentException,PersistenceException{
        Game game=em.find(Game.class,gameId);
        em.remove(game);
    }

    public List<Game> getSavedGames(String login) throws PersistenceException{
        TypedQuery<Game> tq = em.createQuery("SELECT g from Game g join g.players p where p.name=?1", Game.class); //IllegalArgumentException
        tq.setParameter(1, login);
        return tq.getResultList();
    }

    public Game save(Game game){
        return em.merge(game); //IllegalArgumentException, TransactionRequiredException
    }

    public Game load(Integer gameId,String login) throws PersistenceException {
        TypedQuery<Game> tq = em.createQuery("SELECT g FROM Game g join g.players p where g.id=?1 and p.name=?2", Game.class);
        tq.setParameter(1, gameId);
        tq.setParameter(2, login);
        Game game;
        try {game = tq.getSingleResult();} catch (NoResultException e){game= null;} //6 exs
        return game;
    }

    public List<Users> getUsers(String login, long gameId){
        TypedQuery<Users> tq=em.createQuery("select u from Users u join Game g join g.players p where g.id=?1 and p.name=?2",Users.class);
        tq.setParameter(1,gameId);
        tq.setParameter(2,login);
        return tq.getResultList(); //6 exs
    }}

