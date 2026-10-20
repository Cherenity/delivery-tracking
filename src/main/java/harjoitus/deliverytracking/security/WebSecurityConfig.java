package harjoitus.deliverytracking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// Metoditason suojaus lisätty deleteOrder-metodille harjoituksen vuoksi
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
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/api/**").hasRole("ADMIN")
            .requestMatchers("/orders/**", "/editOrder/**",
                "/assignDrivers", "/assignMultiple",
                "/updateOrder", "/updateDelivery")
            .hasRole("ADMIN")
            .anyRequest().authenticated())
        // Käyttää HTTP Basic -authentikointia oletusasetuksilla (Postman)
        .httpBasic(Customizer.withDefaults())
        .formLogin(formlogin -> formlogin
            .loginPage("/login")
            .defaultSuccessUrl("/index", true)
            .permitAll())
        .logout(logout -> logout
            .permitAll())
        .csrf(csrf -> csrf.disable()); //Tätä ei tuotantoon, vain testaukseen

    return http.build();
  }
}
