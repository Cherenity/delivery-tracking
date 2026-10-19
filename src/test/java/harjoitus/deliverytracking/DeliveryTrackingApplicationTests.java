package harjoitus.deliverytracking;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import harjoitus.deliverytracking.web.OrderController;

@SpringBootTest
class DeliveryTrackingApplicationTests {

	@Autowired
	private OrderController controller;

	@Test
	void contextLoads() {
	}

	@Test
	void controllerLoads() {
		assertNotNull(controller);
	}
}