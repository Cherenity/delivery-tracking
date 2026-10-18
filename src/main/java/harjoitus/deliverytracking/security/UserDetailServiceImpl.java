
package harjoitus.deliverytracking.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import harjoitus.deliverytracking.domain.AppUser;
import harjoitus.deliverytracking.repository.AppUserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

  private final AppUserRepository repository;

  public UserDetailServiceImpl(AppUserRepository appUserRepository) {
    this.repository = appUserRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    // Haetaan käyttäjä tietokannasta
    AppUser curruser = repository.findByUsername(username);

    if (curruser == null) {
      throw new UsernameNotFoundException("User not found: " + username);
    }

    // Palautetaan käyttäjän kirjautumistiedot ja rooli
    UserDetails user = new org.springframework.security.core.userdetails.User(
        curruser.getUsername(),
        curruser.getPasswordHash(),
        AuthorityUtils.createAuthorityList("ROLE_" + curruser.getRole()));

    return user;
  }
}
