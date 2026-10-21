package harjoitus.deliverytracking;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import harjoitus.deliverytracking.domain.Order;
import harjoitus.deliverytracking.repository.OrderRepository;

import harjoitus.deliverytracking.domain.AppUser;
import harjoitus.deliverytracking.repository.AppUserRepository;

@SpringBootApplication
public class DeliveryTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryTrackingApplication.class, args);
	}
	// http://localhost:8080/h2-console/ Laitetty ylös
	// http://localhost:8080/orderList

	@Bean
	@Profile("!test") // Ei lisätä demodataa test-profiilin ollessa käytössä
	// Demodataa lisätty
	public CommandLineRunner demoOrderData(OrderRepository orderRepository, AppUserRepository userRepository,
			BCryptPasswordEncoder passwordEncoder) {
		return (args) -> {
			
			// Tyhjennetään vanhat tiedot ennen demodatan lisäämistä
			// CommandLineRunner käyttää sovellukselle määriteltyä tietokantaa
			if (userRepository.count() > 0 || orderRepository.count() > 0) {
				orderRepository.deleteAll();
				userRepository.deleteAll();
			}

			Order order1 = new Order(
					"Cat Food Express - Musti ja Mirri Kamppi",
					"Urho Kekkosen katu 1, 00100 Helsinki",
					LocalDate.now());
			orderRepository.save(order1);

			Order order2 = new Order(
					"Purrfect Delivery - Musti ja Mirri Hakaniemi",
					"Siltasaarenkatu 12, 00530 Helsinki",
					LocalDate.now());
			orderRepository.save(order2);

			Order order3 = new Order(
					"Meow Supplies - Musti ja Mirri Ruoholahti",
					"Itämerenkatu 21, 00180 Helsinki",
					LocalDate.now());
			orderRepository.save(order3);

			// Kuljettajat
			AppUser user1 = new AppUser("ParcelPaws", passwordEncoder.encode("parcel123"), "parcel@example.com", "USER");
			AppUser user2 = new AppUser("PurrExpress", passwordEncoder.encode("express123"), "express@example.com", "USER");

			// Admin
			AppUser admin = new AppUser("DispatchCat", passwordEncoder.encode("dispatch123"), "dispatch@example.com",
					"ADMIN");

			userRepository.save(user1);
			userRepository.save(user2);
			userRepository.save(admin);

		};
	}
}