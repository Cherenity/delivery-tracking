package harjoitus.deliverytracking.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import harjoitus.deliverytracking.domain.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
  AppUser findByUsername(String username);
  List<AppUser> findByRole(String role);
  
}
