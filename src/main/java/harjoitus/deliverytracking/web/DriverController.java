package harjoitus.deliverytracking.web;

import java.time.LocalDate;
import java.util.List;

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
    
    //POOOOOOOOOOOOOOOOOOOOOOOOHDIIIIIIIIIIII KUN POISTIN TOIMINNON SIIRTÄÄ YLÖS ALES JA LISÄSIN MYÖS BULK LISÄYKSEN 
    // Näyttää kuljettajan tämän päivän toimitukset järjestyksessä
    @GetMapping("/myDeliveries/{userId}")
    public String showDeliveries(@PathVariable Long userId, @RequestParam(defaultValue = "false") boolean started,
            Model model) {

        AppUser user = appUserRepository.findById(userId).orElseThrow();
        model.addAttribute("username", user.getUsername());

        LocalDate today = LocalDate.now();

        model.addAttribute("today", today);

        model.addAttribute("orders",
                orderRepository.findByUserIdAndDeliveryDateOrderByDeliveryOrderAscIdAsc(userId, today));

        model.addAttribute("userId", userId);
        model.addAttribute("started", started);

        // tilausten lkm
        long assignedCount = orderRepository.countByUserIdAndDeliveryDateAndStatus(userId, today,
                Order.OrderStatus.ASSIGNED);

        model.addAttribute("assignedCount", assignedCount);

        return "my-deliveries";
    }

    // Aloittaa kuljettajan päivän toimitukset
    @PostMapping("/myDeliveries/{userId}/start")
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

        return "redirect:/myDeliveries/" + userId + "?started=true";
    }

    // Päivittää toimituksen tilan
    @PostMapping("/myDeliveries/{userId}/status")
    public String updateStatus(@PathVariable Long userId,
            @RequestParam Long orderId,
            @RequestParam Order.OrderStatus status) {

        Order order = orderRepository.findById(orderId).orElseThrow();

        // Tarkistetaan, että tilaus kuuluu kuljettajalle (Ehkä ohjaus error sivulle pohditaan)
        if (order.getUser() == null ||
                !order.getUser().getId().equals(userId)) {
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