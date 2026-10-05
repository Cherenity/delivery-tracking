package harjoitus.deliverytracking.repository;

import org.springframework.data.repository.CrudRepository;
import harjoitus.deliverytracking.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
