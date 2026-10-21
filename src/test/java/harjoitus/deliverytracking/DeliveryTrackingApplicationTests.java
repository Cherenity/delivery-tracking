
package harjoitus.deliverytracking;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import harjoitus.deliverytracking.web.OrderController;
import harjoitus.deliverytracking.repository.OrderRepository;
import harjoitus.deliverytracking.repository.AppUserRepository;

@SpringBootTest
@ActiveProfiles("test")
class DeliveryTrackingApplicationTests {

	@Autowired
	private OrderController orderController;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private AppUserRepository appUserRepository;

	@Test// Testaa, että Spring Boot -sovellus käynnistyy ilman virheitä
	void contextLoads() {
	}

	@Test
	void orderControllerLoads() {
		assertNotNull(orderController);
	}

	@Test
	void orderRepositoryLoads() {
		assertNotNull(orderRepository);
	}

	@Test
	void appUserRepositoryLoads() {
		assertNotNull(appUserRepository);
	}
}
