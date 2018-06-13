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

    public Integer getGameLastId() {
        TypedQuery<Integer> typedQuery = em.createQuery("select COALESCE(max(u.id),1) from Game u", Integer.class);
        return typedQuery.getSingleResult();
    }

    public int getPlayerLastId() {
        TypedQuery<Integer> typedQuery = em.createQuery("select COALESCE(max(u.id),1) from Player u", Integer.class);
        return typedQuery.getSingleResult();
    }

}
