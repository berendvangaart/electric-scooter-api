package team3.app.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@NamedQuery(name = "Trip_find_current_from_scooter",
        query = "SELECT s FROM Scooter s WHERE s.status = 'INUSE'")
public class Trip {

    @Id
    @GeneratedValue
    private long id;

    @JsonBackReference
    @ManyToOne
    private Scooter scooter;

    private LocalDateTime start;
    private LocalDateTime end;
    private String gpsLocation;
    private double milage;
    private double cost;

    public Trip() {
    }

    public Trip(Scooter scooter) {
        this.scooter = scooter;
    }

    public Trip(Scooter scooter, LocalDateTime start, String gpsLocation) {
        this.scooter = scooter;
        this.start = start;
        this.gpsLocation = gpsLocation;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Scooter getScooter() {
        return scooter;
    }

    public void setScooter(Scooter scooter) {
        this.scooter = scooter;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public double getMilage() {
        return milage;
    }

    public void setMilage(double milage) {
        this.milage = milage;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


}
