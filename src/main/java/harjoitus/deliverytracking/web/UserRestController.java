package harjoitus.deliverytracking.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import harjoitus.deliverytracking.domain.AppUser;
import harjoitus.deliverytracking.repository.AppUserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class UserRestController {

  private final AppUserRepository appUserRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public UserRestController(AppUserRepository appUserRepository,
      BCryptPasswordEncoder passwordEncoder) {
    this.appUserRepository = appUserRepository;
    this.passwordEncoder = passwordEncoder;
  }

  // Käyttäjien hallinta REST-rajapinnassa toteutettu eri tavalla
  // (ResponseEntityllä määritetään
  // HTTP-tilakoodi, jos suoritus onnistuu, muuten palautuu virhe)
  @PostMapping("/users")
  public ResponseEntity<Void> createUser(@RequestBody Map<String, String> data) {

    // Map vastaanottaa JSON-tiedot avain-arvo-pareina (String, String)
    // data.get() hakee arvon avaimella, encode() hajauttaa salasanan
    AppUser user = new AppUser(
        data.get("username"),
        passwordEncoder.encode(data.get("password")),
        data.get("email"),
        data.get("role"));

    appUserRepository.save(user);

    // Palautetaan 201, jos käyttäjä luotiin onnistuneesti
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

    // Tarkistetaan, löytyykö käyttäjä
    if (!appUserRepository.existsById(id)) {
      // Palautetaan 404 Not Found
      return ResponseEntity.notFound().build();
    }

    appUserRepository.deleteById(id);

    // Palautetaan 200, kun poisto onnistui
    return ResponseEntity.ok().build();
  }

  @GetMapping("/users")
  public ResponseEntity<Iterable<AppUser>> findAllUsers() {

    // Tässä myös 200 (ok = 200)
    return ResponseEntity.ok(appUserRepository.findAll());
  }

  @PutMapping("/users/{id}")
  public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody Map<String, String> data) {

    if (!appUserRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }

    AppUser user = appUserRepository.findById(id).orElseThrow();

    user.setUsername(data.get("username"));
    user.setEmail(data.get("email"));
    user.setRole(data.get("role"));
    user.setPasswordHash(passwordEncoder.encode(data.get("password")));

    appUserRepository.save(user);

    return ResponseEntity.ok().build();
  }

}
