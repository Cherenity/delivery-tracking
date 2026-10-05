package harjoitus.deliverytracking.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "userTable")

public class AppUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false)
  private String passwordHash;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "role", nullable = false)
  private String role;

  public AppUser() {
  }

  public AppUser(String username, String passwordHash, String email, String role) {
    this.username = username;
    this.passwordHash = passwordHash;
    this.email = email;
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public String getEmail() {
    return email;
  }

  public String getRole() {
    return role;
  }

}
