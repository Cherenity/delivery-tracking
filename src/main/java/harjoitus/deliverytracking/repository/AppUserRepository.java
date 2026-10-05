package harjoitus.deliverytracking.repository;

import org.springframework.data.repository.CrudRepository;
import harjoitus.deliverytracking.domain.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
 
}
