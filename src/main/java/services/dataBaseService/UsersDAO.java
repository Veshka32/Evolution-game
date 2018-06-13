package services.dataBaseService;

import game.entities.Game;
import game.entities.Users;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Stateless
public class UsersDAO {

    @PersistenceContext
    private EntityManager em; //because of JTA resource-type

    public boolean isPasswordValid(String login, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {

        try {
            TypedQuery<byte[]> storedPasswordQuery = em.createQuery("select c.password from Users c where c.login=?1", byte[].class);
            storedPasswordQuery.setParameter(1, login);
            byte[] storedPassword = storedPasswordQuery.getSingleResult();

            TypedQuery<byte[]> saltQuery = em.createQuery("select c.salt from Users c where c.login=?1", byte[].class);
            saltQuery.setParameter(1, login);
            byte[] salt = saltQuery.getSingleResult(); //NoResultEx

            return PasswordEncryptionService.authenticate(password, storedPassword, salt);
        } catch (NoResultException e) {
            return false;
        }
    }

    public boolean addUser(String login, String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
        if (!isLoginFree(login)) return false;
        byte[] salt = PasswordEncryptionService.generateSalt();
        Users user = new Users(login, PasswordEncryptionService.getEncryptedPassword(password, salt), salt);
        em.persist(user);
        return true;
    }

    private boolean isLoginFree(String login) {
        TypedQuery<Long> tq = em.createQuery("SELECT c.id FROM Users c where c.login=?1", Long.class);
        tq.setParameter(1, login);
        List<Long> sameLogin = tq.getResultList();
        return sameLogin.size() == 0;
    }

    public Users get(String login){
        TypedQuery<Users> tq = em.createQuery("SELECT c FROM Users c where c.login=?1", Users.class);
        tq.setParameter(1, login);
        return tq.getSingleResult();
    }

    public List<Game> getSavedGames(String login){
        TypedQuery<Game> tq=em.createQuery("select c from Game c join c.players p where p.name=?1",Game.class);
        tq.setParameter(1,login);
        return tq.getResultList();
    }
}