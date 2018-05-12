package services.dataBaseService;

import entities.Users;
import services.dataBaseService.PasswordEncryptionService;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import javax.transaction.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.List;

public class UsersDAO {

    @PersistenceContext
    EntityManager em; //because of JTA resource-type

    public UsersDAO() {
    }

    public boolean isUserValid(String login, String password) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {

        TypedQuery<byte[]> storedPasswordQuery=em.createQuery("select c.password from Users c where c.login=?1",byte[].class);
        storedPasswordQuery.setParameter(1,login);
        byte[] storedPassword=storedPasswordQuery.getSingleResult();

        TypedQuery<byte[]> saltQuery=em.createQuery("select c.salt from Users c where c.login=?1",byte[].class);
        saltQuery.setParameter(1,login);
        byte[] salt=saltQuery.getSingleResult();

        return PasswordEncryptionService.authenticate(password,storedPassword,salt);
    }


    public boolean addUser(String login, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, SystemException, NotSupportedException, NamingException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        if (!isLoginValid(login)) return false;

        byte[] salt = PasswordEncryptionService.generateSalt();
        Users user = new Users(login, PasswordEncryptionService.getEncryptedPassword(password, salt), salt);

        UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        transaction.begin();
        em.persist(user);
        transaction.commit();
        return true;
    }

    public boolean isLoginValid(String login) {
        TypedQuery<Long> tq = em.createQuery("SELECT c.id FROM Users c where c.login=?1", Long.class);
        tq.setParameter(1, login);
        List<Long> sameLogin = tq.getResultList();
        if (sameLogin.size() > 0) return false;
        return true;
    }
}