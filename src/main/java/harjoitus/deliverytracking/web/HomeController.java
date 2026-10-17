package harjoitus.deliverytracking.web;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import harjoitus.deliverytracking.domain.Order;
import harjoitus.deliverytracking.repository.OrderRepository;

@Controller
public class HomeController {

  private final OrderRepository orderRepository;

  public HomeController(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  // Näyttää etusivun ja tämän päivän toimitustilastot
  @GetMapping({ "/", "/index" })
  public String index(Model model) {

    LocalDate today = LocalDate.now();

    model.addAttribute("totalCount",
        orderRepository.countByDeliveryDate(today));

    model.addAttribute("deliveredCount",
        orderRepository.countByDeliveryDateAndStatus(
            today, Order.OrderStatus.DELIVERED));

    model.addAttribute("inProgressCount",
        orderRepository.countByDeliveryDateAndStatus(
            today, Order.OrderStatus.IN_PROGRESS));

    return "index";
  }
}