package harjoitus.deliverytracking.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import harjoitus.deliverytracking.repository.OrderRepository;


@Controller
public class OrderController {
  private final OrderRepository orderRepository;

  public OrderController(OrderRepository orderRepository) {
      this.orderRepository = orderRepository;
  }

  @GetMapping("/orderlist")
  public String testMethod(Model model) {
      model.addAttribute("orders", orderRepository.findAll());
      return "orderlist";
  }
  

}
