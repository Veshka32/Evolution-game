package services.dataBaseService;

import entities.Users;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class Test {

    @PersistenceContext private EntityManager em;

    public void addUser(String name) {

        Users e = new Users(name);
        em.persist(e);
    }

}
