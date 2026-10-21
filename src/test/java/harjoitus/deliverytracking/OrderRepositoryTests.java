package harjoitus.deliverytracking;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import harjoitus.deliverytracking.domain.Order;
import harjoitus.deliverytracking.repository.OrderRepository;

@DataJpaTest
@ActiveProfiles("test")
public class OrderRepositoryTests {

  @Autowired
  private OrderRepository orderRepository;

  // Testaa uuden tilauksen tallentamista tietokantaan
  @Test
  public void createNewOrder() {
    Order order = new Order(
        "MeowParcel",
        "Länsituuli 5, 02100 Espoo",
        LocalDate.now());

    order.setDeliveryOrder(1);

    orderRepository.save(order);

    assertThat(order.getId()).isNotNull();
  }

  // Testaa kaikkien tilausten hakemista tietokannasta
  @Test
  public void findAllOrders() {
    Iterable<Order> orders = orderRepository.findAll();

    assertThat(orders).hasSize(5);
  }
}
