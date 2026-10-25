package harjoitus.deliverytracking.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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

    // Kuljettajien tämän päivän toimitustilastot
    List<Map<String, Object>> driverStat = new ArrayList<>();
    List<AppUser> users  = appUserRepository.findByRole("USER");
    model.addAttribute("users", users);

    for (AppUser driver : users) {
      Long userId = driver.getId();

      int total = orderRepository
          .findByUserIdAndDeliveryDate(userId, today).size();
      int inProgress = orderRepository
          .countByUserIdAndDeliveryDateAndStatus(
              userId, today, Order.OrderStatus.IN_PROGRESS);

      int failed = orderRepository
          .countByUserIdAndDeliveryDateAndStatus(
              userId, today, Order.OrderStatus.FAILED_DELIVERY);

      int delivered = orderRepository
          .countByUserIdAndDeliveryDateAndStatus(
              userId, today, Order.OrderStatus.DELIVERED);

      Map<String, Object> stats = new HashMap<>();


      stats.put("username", driver.getUsername());
      stats.put("total", total);
      stats.put("inProgress", inProgress);
      stats.put("failed", failed);
      stats.put("delivered", delivered);

      // Lisätään kuljettajan tilastot listaan
      driverStat.add(stats);
      
    }

    model.addAttribute("driverStat", driverStat);

    model.addAttribute("totalOrderCount",
        orderRepository.count());

    model.addAttribute("totalCount",
        orderRepository.countByDeliveryDate(today));

    model.addAttribute("unassignedToday",
        orderRepository.countByDeliveryDateAndStatus(today, Order.OrderStatus.UNASSIGNED));

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

  @GetMapping("/testError")
  public String testError() {

    // throw new IllegalArgumentException("This is a test error (400)");

    throw new NoSuchElementException("Driver not found (404)");
  }

}