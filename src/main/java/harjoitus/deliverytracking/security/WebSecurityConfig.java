
package harjoitus.deliverytracking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/css/**", "/login").permitAll()
            .requestMatchers("/orderList/**", "/modifyOrder/**",
                "/assignDrivers", "/assignMultiple",
                "/updateOrder", "/updateDelivery",
                "/deleteOrder/**")
            .hasRole("ADMIN")
            .anyRequest().authenticated())
        .formLogin(formlogin -> formlogin
            .loginPage("/login")
            .defaultSuccessUrl("/index", true)
            .permitAll())
        .logout(logout -> logout
            .permitAll());

    return http.build();
  }
}
