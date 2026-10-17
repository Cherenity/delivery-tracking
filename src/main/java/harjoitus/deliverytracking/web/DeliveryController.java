package harjoitus.deliverytracking.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import harjoitus.deliverytracking.domain.Order;
import harjoitus.deliverytracking.repository.OrderRepository;

@Controller
public class DeliveryController {

    private final OrderRepository orderRepository;

    public DeliveryController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Näyttää kuljettajan toimitukset järjestyksessä
    // Näyttää kuljettajan tämän päivän toimitukset järjestyksessä
    @GetMapping("/deliveryList/{userId}")
    public String showDeliveries(@PathVariable Long userId, @RequestParam(defaultValue = "false") boolean started,
            Model model) {

        LocalDate today = LocalDate.now();

        model.addAttribute("orders",
                orderRepository.findByUserIdAndDeliveryDateOrderByDeliveryOrderAscIdAsc(userId, today));

        model.addAttribute("userId", userId);
        model.addAttribute("started", started);

        // tilausten lkm
        int assignedCount = orderRepository.countByUserIdAndDeliveryDateAndStatus(userId, today, Order.OrderStatus.ASSIGNED);
                
        model.addAttribute("assignedCount", assignedCount);

        return "deliveryList";
    }

    // Aloittaa kuljettajan päivän toimitukset
    @PostMapping("/deliveryList/{userId}/start")
    public String startDeliveries(@PathVariable Long userId) {

        // Haetaan kuljettajan tämän päivän tilaukset
        List<Order> orders = orderRepository.findByUserIdAndDeliveryDateOrderByDeliveryOrderAscIdAsc(userId,
                LocalDate.now());

        // Muutetaan tila IN_PROGRESS
        for (Order order : orders) {
            if (order.getStatus() == Order.OrderStatus.ASSIGNED) {
                order.setStatus(Order.OrderStatus.IN_PROGRESS);
                orderRepository.save(order);
            }
        }

        return "redirect:/deliveryList/" + userId + "?started=true";
    }

    // Päivittää toimituksen tilan
    @PostMapping("/deliveryList/{userId}/status")
    public String updateStatus(@PathVariable Long userId,
            @RequestParam Long orderId,
            @RequestParam Order.OrderStatus status) {

        Order order = orderRepository.findById(orderId).orElseThrow();

        // Tarkistetaan, että tilaus kuuluu kuljettajalle
        if (order.getUser() == null ||
                !order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Invalid driver");
        }

        // Sallitaan vain aloitetun toimituksen kuittaaminen
        if (order.getStatus() == Order.OrderStatus.IN_PROGRESS &&
                (status == Order.OrderStatus.DELIVERED ||
                        status == Order.OrderStatus.FAILED_DELIVERY)) {

            order.setStatus(status);
            orderRepository.save(order);
        }

        return "redirect:/deliveryList/" + userId;
    }

}