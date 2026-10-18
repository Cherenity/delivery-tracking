package harjoitus.deliverytracking.web;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.security.core.Authentication;
import harjoitus.deliverytracking.domain.AppUser;

import harjoitus.deliverytracking.domain.Order;
import harjoitus.deliverytracking.repository.AppUserRepository;
import harjoitus.deliverytracking.repository.OrderRepository;

@Controller
public class HomeController {

  private final OrderRepository orderRepository;
  private final AppUserRepository appUserRepository;

  public HomeController(OrderRepository orderRepository, AppUserRepository appUserRepository) {
    this.orderRepository = orderRepository;
    this.appUserRepository = appUserRepository;
  }

  // Näyttää etusivun ja tämän päivän toimitustilastot
  @GetMapping({ "/", "/index" })
  public String index(Model model, Authentication authentication) {
    AppUser user = appUserRepository.findByUsername(authentication.getName());
    model.addAttribute("userId", user.getId());
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

  // Näyttää kirjautumissivun
  @GetMapping("/login")
  public String login() {
    return "login";
  }

}