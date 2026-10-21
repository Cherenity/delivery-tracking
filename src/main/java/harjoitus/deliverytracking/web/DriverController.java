package harjoitus.deliverytracking.web;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import harjoitus.deliverytracking.domain.AppUser;
import harjoitus.deliverytracking.domain.Order;
import harjoitus.deliverytracking.repository.AppUserRepository;
import harjoitus.deliverytracking.repository.OrderRepository;

@Controller
public class DriverController {

    private final OrderRepository orderRepository;
    private final AppUserRepository appUserRepository;

    public DriverController(OrderRepository orderRepository, AppUserRepository appUserRepository) {
        this.orderRepository = orderRepository;
        this.appUserRepository = appUserRepository;
    }

    // Näyttää kuljettajan tämän päivän toimitukset  
    @GetMapping("/myDeliveries/{userId}")
    public String showDeliveries(@PathVariable Long userId, Model model) {

        LocalDate today = LocalDate.now();

        model.addAttribute("today", today);
        model.addAttribute("orders", orderRepository
                .findByUserIdAndDeliveryDate(userId, today));

        AppUser user = appUserRepository
                .findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Driver not found"));
        model.addAttribute("username", user.getUsername());

        model.addAttribute("userId", userId);

        // määritettyjen tilausten lkm kyseisenä päivänä
        long assignedCount = orderRepository.countByUserIdAndDeliveryDateAndStatus(userId, today,
                Order.OrderStatus.ASSIGNED);
        model.addAttribute("assignedCount", assignedCount);

        return "my-deliveries";
    }

    // Aloittaa kuljettajan päivän toimitukset
    @PostMapping("/myDeliveries/{userId}/start")
    public String startDeliveries(@PathVariable Long userId) {
        LocalDate today = LocalDate.now();

        // Haetaan kuljettajan tämän päivän tilaukset
        List<Order> orders = orderRepository
                .findByUserIdAndDeliveryDate(userId, today);

        // Muutetaan tila IN_PROGRESS
        for (Order order : orders) {
            if (order.getStatus() == Order.OrderStatus.ASSIGNED) {
                order.setStatus(Order.OrderStatus.IN_PROGRESS);
                orderRepository.save(order);
            }
        }
        return "redirect:/myDeliveries/" + userId;
    }

    // Päivittää toimituksen tilan
    @PostMapping("/myDeliveries/{userId}/status")
    public String updateStatus(@PathVariable Long userId,
            @RequestParam Long orderId,
            @RequestParam Order.OrderStatus status) {

        Order order = orderRepository.findById(orderId).orElseThrow();

        // Tarkistetaan, että tilaus kuuluu kuljettajalle
        if (order.getUser() == null || !order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Invalid driver");
        }

        // Sallitaan vain aloitetun toimituksen päivittäminen
        if (order.getStatus() == Order.OrderStatus.ASSIGNED ||
                order.getStatus() == Order.OrderStatus.UNASSIGNED ||
                order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Delivery not in progress");
        }

        // Sallitaan vain toimitettu tai epäonnistunut
        if (status != Order.OrderStatus.DELIVERED &&
                status != Order.OrderStatus.FAILED_DELIVERY) {
            throw new IllegalArgumentException("Invalid status");
        }

        order.setStatus(status);
        orderRepository.save(order);

        return "redirect:/myDeliveries/" + userId;
    }

}