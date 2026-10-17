package harjoitus.deliverytracking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import harjoitus.deliverytracking.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
    // findByUserId-metodi palauttaa listan Order-olioita, jotka on liitetty
    // tiettyyn käyttäjään (userId)
    List<Order> findByUserId(Long userId);

    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html

    // Hakee kuljettajan tietyn päivän tilaukset toimitusjärjestyksessä
    List<Order> findByUserIdAndDeliveryDateOrderByDeliveryOrderAscIdAsc(
            Long userId, LocalDate deliveryDate);

    // Laskee kuljettajan tietyn päivän tilaukset annetun tilan perusteella
    int countByUserIdAndDeliveryDateAndStatus(
            Long userId, LocalDate deliveryDate, Order.OrderStatus status);
    
    //HomeController
    // Laskee tietyn päivän kaikki tilaukset
    int countByDeliveryDate(LocalDate deliveryDate);

    // Laskee tietyn päivän tilaukset annetun statuksen perusteella
    int countByDeliveryDateAndStatus(
            LocalDate deliveryDate, Order.OrderStatus status);

}
