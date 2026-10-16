package harjoitus.deliverytracking.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import harjoitus.deliverytracking.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
  // findByUserId-metodi palauttaa listan Order-olioita, jotka on liitetty tiettyyn käyttäjään (userId)
  List<Order> findByUserId(Long userId);
}
