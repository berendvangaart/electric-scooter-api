package team3.app.repositories;

import team3.app.models.Scooter;

import java.util.List;

public interface ScooterRepository {

  List<Scooter> findAll();

  Scooter findById(Long id);

  Scooter save(Scooter scooter);

  boolean deleteById(long id);

  List<Scooter> findByQuery(String jpqlName, Object... params);

}
