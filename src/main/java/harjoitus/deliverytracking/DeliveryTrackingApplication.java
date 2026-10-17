package harjoitus.deliverytracking;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
	// Demodataa lisätty
	public CommandLineRunner demoOrderData(OrderRepository orderRepository, AppUserRepository userRepository) {
		return (args) -> {

			Order order1 = new Order(
					"Prisma",
					"Mannerheimintie 1",
					LocalDate.now()
			);
			orderRepository.save(order1);

			Order order2 = new Order(
					"K-Market",
					"Aleksanterinkatu 10",
					LocalDate.now(),
					Order.OrderStatus.ASSIGNED);
			orderRepository.save(order2);

			Order order3 = new Order(
					"Lidl",
					"Hämeentie 20",
					LocalDate.now(),
					Order.OrderStatus.IN_PROGRESS);
			orderRepository.save(order3);

			AppUser user1 = new AppUser("user1", "NOT_CONFIGURED", "user1@example.com", "USER");
			AppUser user2 = new AppUser("user2", "NOT_CONFIGURED", "user2@example.com", "USER");

			userRepository.save(user1);
			userRepository.save(user2);

		};
	}
}