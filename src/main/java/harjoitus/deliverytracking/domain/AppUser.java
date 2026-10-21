package harjoitus.deliverytracking.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "userTable")
public class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false)
  // Estää salasanatiivisteen näkymisen REST-rajapinnan JSON-vastauksissa
  // Menee sisään, mutta ei ulos
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String passwordHash;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "role", nullable = false)
  private String role;

  @OneToMany (mappedBy = "user")
  private java.util.List<Order> orders;

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

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}