package harjoitus.deliverytracking;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import harjoitus.deliverytracking.domain.Order;
import harjoitus.deliverytracking.repository.OrderRepository;

@DataJpaTest
@ActiveProfiles("test")
public class OrderRepositoryTests {

  @Autowired
  private OrderRepository orderRepository;

  // Testaa uuden tilauksen tallentamista tietokantaan
  @Test
  @Transactional
  public void createNewOrder() {
    Order order = new Order(
        "Musti ja Mirri Espoo Tapiola",
        "Länsituulentie 12, 02100 Espoo",
        LocalDate.now());

    orderRepository.save(order);

    assertThat(order.getId()).isNotNull();
  }

  // Testaa kaikkien tilausten hakemista tietokannasta
  @Test
  @Transactional
  public void findAllOrders() {
    assertThat(orderRepository.findAll()).hasSize(5);
  }

  // Testaa tilauksen poistamista tietokannasta
  @Test
  @Transactional
  public void deleteOrder() {

    Order order = new Order(
        "Musti ja Mirri Porvoo",
        "Käpäläkuja 7, 06100 Porvoo",
        LocalDate.now());

    orderRepository.save(order);

    Long orderId = order.getId();

    orderRepository.deleteById(orderId);

    assertThat(orderRepository.existsById(orderId)).isFalse();
  }

  // Testaa tietylle kuljettajalle määritettyjen tilausten hakemista
  @Test
  public void findOrdersByUserId() {
    var orders = orderRepository.findByUserId(2L);

    assertThat(orders).hasSize(3);
  }

}
