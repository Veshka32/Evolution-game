package services.dataBaseService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class IdDAO {
    @PersistenceContext
    private EntityManager em;

    public int getGameLastId() {
        TypedQuery<Integer> typedQuery = em.createQuery("select max(id) from Game", Integer.class);
        Integer maxId = 0;
        try {
            maxId = typedQuery.getSingleResult();
        } catch (NoResultException e) {
        } finally {
            return maxId;
        }
    }

    public int getPlayerLastId() {
        TypedQuery<Integer> typedQuery = em.createQuery("select max(id) from Player", Integer.class);
        Integer maxId = 0;
        try {
            maxId = typedQuery.getSingleResult();
        } catch (NoResultException e) {
        } finally {
            return maxId;
        }
    }

}
