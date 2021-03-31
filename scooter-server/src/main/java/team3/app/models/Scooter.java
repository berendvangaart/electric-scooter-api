package team3.app.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
  @NamedQuery(name="Scooter_find_by_status",
  query="SELECT s from Scooter s where s.status = ?1"),
  @NamedQuery(name="Scooter_find_by_battery",
    query = "SELECT s FROM Scooter s where s.battery < ?1"
  )
})
public class Scooter {

  public static int hiID = 3000;

  @Id
  @GeneratedValue
  @JsonView(Scooter.hideLocationView.class)
  private long id;
  @JsonView(Scooter.hideLocationView.class)
  private String tag;
  @Enumerated(EnumType.STRING)
  @JsonView(Scooter.hideLocationView.class)
  private Status status;
  private String gpsLocation;
  @JsonView(Scooter.hideLocationView.class)
  private int milage;
  @JsonView(Scooter.hideLocationView.class)
  private int battery;
  @JsonManagedReference
  @JsonView(Scooter.hideLocationView.class)
  @OneToMany(mappedBy = "scooter")
  private List<Trip> trips = new ArrayList<>();

  public Scooter() {
  }

  public Scooter(String tag) {
    this.tag = tag;
  }

  public Long getId() {
    return id;
  }

  public String getTag() {
    return tag;
  }

  public Status getStatus() {
    return status;
  }

  public String getGpsLocation() {
    return gpsLocation;
  }

  public int getMilage() {
    return milage;
  }

  public int getBattery() {
    return battery;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public void setGpsLocation(String gpsLocation) {
    this.gpsLocation = gpsLocation;
  }

  public void setMilage(int milage) {
    this.milage = milage;
  }

  public void setBattery(int battery) {
    this.battery = battery;
  }

  public void addTrip(Trip t) {
    trips.add(t);
  }

  public void removeTrip(Trip t) {
    trips.remove(t);
  }

  public enum Status {
    IDLE,
    INUSE,
    MAINTENANCE
  }

  public Trip startNewTrip(LocalDateTime start) {
    Trip tr = new Trip(this, start, this.gpsLocation);
    this.addTrip(tr);
    this.setStatus(Status.INUSE);
    tr.setScooter(this);
    return tr;
  }



  public static Scooter createRandomScooter(){
    Scooter sc = new Scooter();

    sc.setTag("newScooter");
    sc.setStatus(Status.IDLE);
    String lat = Integer.toString((int) (Math.random() * 100 + 100));
    String lng = Integer.toString((int) (Math.random() * 100 + 10));
    sc.setGpsLocation("lat: 52.37" + lat + " lng: 4.895" + lng);
    sc.setMilage(((int) (Math.random()*(3000))));
    sc.setBattery(((int) (Math.random()*(100))));

    return sc;
  }

  public static class hideLocationView{}



}

