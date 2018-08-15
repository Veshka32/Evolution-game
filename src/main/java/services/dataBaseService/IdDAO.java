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
        TypedQuery<Integer> typedQuery = em.createQuery("select COALESCE(max(g.id),1) from Game g", Integer.class);
        return typedQuery.getSingleResult();
    }

    public int getPlayerLastId() {
        TypedQuery<Integer> typedQuery = em.createQuery("select COALESCE(max(p.id),1) from Player p", Integer.class);
        return typedQuery.getSingleResult();
    }

}
