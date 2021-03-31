package team3.app.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
  @Id
  private long id;
  private String name;
  private String email;
  private String hashedPassword;
  private boolean admin;

  private static Long autoID = 0L;

  public User(String name, String email, String hashedPassword, boolean admin) {
    this.name = name;
    this.email = email;
    this.hashedPassword = hashedPassword;
    this.admin = admin;
  }

  public User() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getHashedPassword() {
    return hashedPassword;
  }

  public void setHashedPassword(String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }


  public static Long getAutoID() {
    Long newID = autoID;
    autoID ++;
    return newID;
  }
}
