//
//package team3.app.repositories;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.*;
//import team3.app.models.Scooter;
//
//import java.util.Arrays;
//import java.util.List;
//
//
//@Component
//public class ScooterRepositoryMock implements ScooterRepository {
//  private final Scooter[] SCOOTERS = new Scooter[7];
//  //private final ArrayList<Scooter> SCOOTERS = new ArrayList<>();
//
//  public ScooterRepositoryMock() {
//    SCOOTERS[0] = Scooter.createRandomScooter();
//    SCOOTERS[1] = Scooter.createRandomScooter();
//    SCOOTERS[2] = Scooter.createRandomScooter();
//  }
//
//
//  @Override
//  public List<Scooter> findAll() {
//    return Arrays.asList(SCOOTERS);
//  }
//
//
//  @Override
//  public Scooter findById(@PathVariable Long id) {
//    try {
//      for (Scooter s : SCOOTERS) {
//        if (s.getId() == id) {
//          return s;
//        }
//      }
//    } catch (Exception e) {
//
//    }
//    return null;
//  }
//
//
//  @Override
//  public Scooter save(Scooter scooter) {
//    //sets the id to new id if scooter id = 0
//    if (scooter.getId() == 0) {
//      Scooter.hiID++;
//      scooter.setId(Scooter.hiID);
//    }
//
//    //delete scooter if already exist
//    try {
//      this.deleteById(scooter.getId());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//
//    //sets the scooter on a empry place in the array
//    for (int i = 0; i < SCOOTERS.length; i++) {
//      if (SCOOTERS[i] == null) {
//        SCOOTERS[i] = scooter;
//        return scooter;
//      }
//    }
//    return null;
//  }
//
//  @Override
//  public boolean deleteById(long id) {
//    for (int i = 0; i < SCOOTERS.length; i++) {
//      if (SCOOTERS[i] != null) {
//        if (SCOOTERS[i].getId() == id) {
//          SCOOTERS[i] = null;
//          return true;
//        }
//      }
//    }
//    return false;
//
////    for (int i = 0; i < SCOOTERS.length; i++) {
////      try {
////        if (SCOOTERS[i].getId() == id) {
////          SCOOTERS[i] = null;
////
////        }
////        return true;
////      } catch (Exception e) {
////      }
////    }
////    return false;
//  }
//
//  @Override
//  public List<Scooter> findByQuery(String jpqlName, Scooter scooter) {
//    return null;
//  }
//}
//
