package harjoitus.deliverytracking.web;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import harjoitus.deliverytracking.domain.Order;

import harjoitus.deliverytracking.repository.OrderRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class OrderRestController {

  private final OrderRepository orderRepository;

  public OrderRestController(OrderRepository orderRepository,
      BCryptPasswordEncoder passwordEncoder) {
    this.orderRepository = orderRepository;
  }

  @GetMapping("/orders")
  public Iterable<Order> findAllOrders() {
    return orderRepository.findAll();
  }

  @GetMapping("/orders/{userId}")
  public Iterable<Order> findOrdersByUserId(@PathVariable Long userId) {
    return orderRepository.findByUserId(userId);
  }

  @PostMapping("/orders")
  public Order createOrder(@RequestBody Order order) {
    order.setStatus(Order.OrderStatus.UNASSIGNED);
    return orderRepository.save(order);
  }

  @PostMapping("/orders/bulk")
  public Iterable<Order> addOrders(@RequestBody List<Order> orders) {
    return orderRepository.saveAll(orders);
  }

  // Voi poistaa useita
  @DeleteMapping("/orders/bulk")
  public void deleteOrders(@RequestBody List<Long> ids) {
    orderRepository.deleteAllById(ids);
  }

  @DeleteMapping("/orders/{id}")
  public void deleteOrder(@PathVariable Long id) {
    orderRepository.deleteById(id);
  }

  @PutMapping("/orders/{id}")
  public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {

    Order existingOrder = orderRepository.findById(id).orElseThrow();

    existingOrder.setCustomer(order.getCustomer());
    existingOrder.setAddress(order.getAddress());
    existingOrder.setDeliveryDate(order.getDeliveryDate());

    return orderRepository.save(existingOrder);
  }

}
