package team3.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import team3.app.models.Scooter;
import team3.app.models.Trip;
import team3.app.repositories.ScooterRepositoryJpa;
import team3.app.repositories.TripsRepositoryJPA;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
	  SpringApplication.run(Application.class, args);
	}

  @Autowired
  ScooterRepositoryJpa sr;
	@Autowired
	TripsRepositoryJPA tr;


	@Transactional
  @Override
  public void run(String... args) throws Exception {

    List<Scooter> scooters = sr.findAll();

    if (scooters.size() > 0) {return;}

    System.out.println("configure some initial scooters");

    for (int i = 0; i < 4; i++) {
      Scooter scooter = Scooter.createRandomScooter();
     Trip trip = scooter.startNewTrip(LocalDateTime.now());
//      Trip trip2 = scooter.startNewTrip(LocalDateTime.now());
//      Trip trip3 = scooter.startNewTrip(LocalDateTime.now());
//
//
      sr.save(scooter);

      tr.save(trip);
//      tr.save(trip2);
//      tr.save(trip3);
    }



  }


}
