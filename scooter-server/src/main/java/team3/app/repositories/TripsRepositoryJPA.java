package team3.app.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team3.app.models.Scooter;
import team3.app.models.Trip;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class TripsRepositoryJPA {

  @Autowired
  EntityManager em;

  public List<Trip> findAll() {
    return em.createQuery("select T from Trip t", Trip.class).getResultList();
  }

  public Trip findById(Long id) {
    return em.find(Trip.class, id);
  }

  public Trip save(Trip t) {
    if (t.getId() == null) {
      em.persist(t);
    } else {
      em.merge(t);
    }
   return t;
  }

  public boolean delete(Long id) {
    if (id != null) {
      Trip t = this.findById(id);
      em.remove(t);
      return true;
    }
    return false;
  }

  public List<Scooter> findByQuery(String jpqlName, Object... params) {
    TypedQuery<Scooter> q = em.createNamedQuery(jpqlName, Scooter.class);

    return q.getResultList();
  }

}
