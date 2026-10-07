package harjoitus.deliverytracking;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import harjoitus.deliverytracking.domain.Order;
import harjoitus.deliverytracking.domain.OrderStatus;
import harjoitus.deliverytracking.repository.OrderRepository;

@SpringBootApplication
public class DeliveryTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryTrackingApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoOrderData(OrderRepository orderRepository) {
		return (args) -> {

			Order order1 = new Order(
				"Prisma",
				"Mannerheimintie 1",
				LocalDate.of(2026, 10, 8),
				OrderStatus.UNASSIGNED
			);
			orderRepository.save(order1);

			Order order2 = new Order(
				"K-Market",
				"Aleksanterinkatu 10",
				LocalDate.of(2026, 10, 8),
				OrderStatus.UNASSIGNED
			);
			orderRepository.save(order2);

			Order order3 = new Order(
				"Lidl",
				"Hämeentie 20",
				LocalDate.of(2026, 10, 9),
				OrderStatus.ASSIGNED
			);
			orderRepository.save(order3);

		};
	}
}