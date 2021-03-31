package team3.app.rest;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import team3.app.models.Scooter;
import team3.app.models.Trip;
import team3.app.repositories.ScooterRepositoryJpa;
import team3.app.repositories.TripsRepositoryJPA;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class ScooterController {

    @Autowired
    private ScooterRepositoryJpa sr;
    @Autowired
    private TripsRepositoryJPA tr;
//  private ScooterRepositoryMock sc;

    @GetMapping(path = "/scooters")
    public ResponseEntity<List<Scooter>> getAllScooters(
            @RequestParam(required = false) String battery,
            @RequestParam(required = false) String status
    ) {
        //checks if too many parameters are provided
        if (status != null && battery != null) {
            throw new PreConditionFailed("Too many parameters where provided");
        }
        //check if valid battery
        if (battery != null) {
            try {
                Integer.parseInt(battery);
            } catch (NumberFormatException e) {
                throw new PreConditionFailed("enter a valid number for the battery");
            }
        }

        if (battery != null) {
            if (Integer.parseInt(battery) > 100 || Integer.parseInt(battery) < 0) {
                throw new PreConditionFailed(Integer.parseInt(battery) + " is not a valid number");
            }
        }

        //checks status
        if (status != null) {
            for (Scooter.Status st : Scooter.Status.values()) {
                if (st.name().equals(status)) {
                    List<Scooter> sc = sr.findByQuery("Scooter_find_by_status", st);
                    return new ResponseEntity<>(sc, HttpStatus.OK);
                }
            }
        }
        if (status != null) {
            throw new PreConditionFailed(status + " is not a valid staus");
        }
        //calls named query for battery
        if (battery != null) {
            List<Scooter> sc = sr.findByQuery("Scooter_find_by_battery", Integer.parseInt(battery));
            return new ResponseEntity<>(sc, HttpStatus.OK);
        }
        //shows all scooters
        List<Scooter> allScooters = sr.findAll();
        return allScooters == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(allScooters, HttpStatus.OK);
    }

    @GetMapping("/scooters/currenttrips")
    public ResponseEntity<List<Scooter>> getCurrentTrips() {
        List<Scooter> trips = tr.findByQuery("Trip_find_current_from_scooter");
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @JsonView(Scooter.hideLocationView.class)
    @GetMapping(path = "/scooters/summary")
    public ResponseEntity<List<Scooter>> getScooterSummary() {
        List<Scooter> allScooters = sr.findAll();
        return allScooters == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<List<Scooter>>(allScooters, HttpStatus.OK);
    }

    @GetMapping("/scooters/{id}")
    public Scooter findById(@PathVariable Long id) {
        Scooter scooter = sr.findById(id);

        if (scooter == null) {
            throw new ResourceNotFoundException("Scooter with id: " + id + " does not exist");
        } else return scooter;

    }

    @PostMapping("/scooters")
    public ResponseEntity<Scooter> save(@RequestBody Scooter scooter) {

        Scooter savedScooter = sr.save(scooter);

        if (savedScooter != null) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
                    buildAndExpand(savedScooter.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(savedScooter);
        }
        return ResponseEntity.badRequest().body(savedScooter);
    }

    @PutMapping("/scooters/{id}")
    public ResponseEntity<Scooter> updateScooter(@PathVariable long id, @RequestBody Scooter scooter) {

        if (id != scooter.getId()) {
            throw new PreConditionFailed("the path variable id " + id + " does not match " + scooter.getId());
        }

        Scooter tempScooter = sr.save(scooter);

        if (tempScooter != null) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
                    buildAndExpand(tempScooter.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(tempScooter);
        }
        return ResponseEntity.badRequest().body(tempScooter);
    }

    @PostMapping("scooters/{scooterId}/claim")
    public ResponseEntity<Scooter> claimScooter(@PathVariable long scooterId, @RequestBody Optional<LocalDateTime> start) {
        //if scooter?
        Scooter sc = sr.findById(scooterId);
        if (sc == null) {
            throw new PreConditionFailed("there is no scooter with a id of: " + scooterId);
        }
        //if scooter is not in use or battery is too low
        if (sc.getStatus() != Scooter.Status.IDLE || sc.getBattery() < 10) {
            throw new PreConditionFailed("Scooter " + scooterId + " is not available");
        }
        LocalDateTime startTime;
        startTime = start.isPresent() ? start.get() : LocalDateTime.now();

        Trip trip = sc.startNewTrip(startTime);
        tr.save(trip);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{sc}").
                buildAndExpand(sc.getId()).toUri();

        return ResponseEntity.created(location)
                .body(sc);


    }

    @DeleteMapping("/scooters/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable long id) {
        boolean deleted = sr.deleteById(id);

        if (!deleted) {
            throw new ResourceNotFoundException("can't delete scooter with id : " + id +
                    " scooter does not exist");
        }

        return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {

        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public static class PreConditionFailed extends RuntimeException {
        public PreConditionFailed(String message) {
            super(message);
        }
    }


}
