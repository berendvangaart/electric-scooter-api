package team3.app.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team3.app.models.Scooter;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ScooterRepositoryJpa implements ScooterRepository {

    @Autowired
    EntityManager em;

    /**
     * retrieves all scooters in the H2db
     *
     * @return list of scooters objects
     */
    @Override
    public List<Scooter> findAll() {
        TypedQuery<Scooter> query = em.createQuery("SELECT s FROM Scooter s", Scooter.class);
        return query.getResultList();
    }

    @Override
    public Scooter findById(Long id) {
        return em.find(Scooter.class, id);
    }

    /**
     * Saves or updates scooter
     *
     * @param scooter new or updated scooter
     * @return - added or updated scooter
     */
    @Override
    public Scooter save(Scooter scooter) {
        if (scooter.getId() == 0) {
            //new scooter
            em.persist(scooter);
        } else {
            //update scooter
            em.merge(scooter);
        }

        return scooter;
    }

    @Override
    public boolean deleteById(long id) {
        Scooter s = findById(id);
        if (s != null) {
            em.remove(s);
            return true;
        }
        return false;
    }

    @Override
    public List<Scooter> findByQuery(String jpqlName, Object... params) {
        TypedQuery<Scooter> q = em.createNamedQuery(jpqlName, Scooter.class);

        return q.setParameter(1, params[0])
                .getResultList();
    }

}
