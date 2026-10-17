package harjoitus.deliverytracking.web;

import harjoitus.deliverytracking.repository.AppUserRepository;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import harjoitus.deliverytracking.domain.AppUser;
import harjoitus.deliverytracking.domain.Order;
import harjoitus.deliverytracking.repository.OrderRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;

// http://localhost:8080/orderList

@Controller
public class OrderController {

    private final AppUserRepository appUserRepository;
    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository, AppUserRepository appUserRepository) {
        this.orderRepository = orderRepository;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/orderList")
    public String showOrders(Model model) {
        
        model.addAttribute("orders", orderRepository.findAll());
        // Yksi tyhjä Order-olio lomaketta varten

        Order order = new Order();
        LocalDate today = LocalDate.now();

        order.setDeliveryDate(today);

        model.addAttribute("order", order);
        
        model.addAttribute("users", appUserRepository.findAll());

        return "orderList";
    }

    @PostMapping("/orderList")
    public String addOrder(@Valid @ModelAttribute("order") Order order, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("orders", orderRepository.findAll());
            model.addAttribute("users", appUserRepository.findAll());
            return "orderList";
        }

        orderRepository.save(order);

        return "redirect:/orderList";
    }

    // Pohditaan vielä
    @GetMapping("/orderList/{userId}")
    public String showDriverOrders(@PathVariable Long userId, Model model) {

        model.addAttribute("orders", orderRepository.findByUserId(userId));
        model.addAttribute("order", new Order());
        model.addAttribute("users", appUserRepository.findAll());

        return "orderList";
    }

    @PostMapping("/assignDriver")
    public String assignDriver(@RequestParam Long orderId, @RequestParam(required = false) Long userId) {

        Order order = orderRepository.findById(orderId).orElseThrow();

        if (userId == null) {
            order.setUser(null);
            order.setStatus(Order.OrderStatus.UNASSIGNED);
            orderRepository.save(order);
            return "redirect:/orderList";
        }

        AppUser user = appUserRepository.findById(userId).orElseThrow();

        order.setUser(user);
        order.setStatus(Order.OrderStatus.ASSIGNED);

        orderRepository.save(order);

        return "redirect:/orderList";
    }

    @GetMapping("/modifyOrder/{id}")
    public String modifyOrder(@PathVariable Long id, Model model) {

        Order order = orderRepository.findById(id).orElseThrow();

        model.addAttribute("order", order);

        return "modifyOrder";
    }

    @PostMapping("/updateOrder")
    public String updateOrder(@ModelAttribute Order order) {

        Order existingOrder = orderRepository.findById(order.getId()).orElseThrow();

        existingOrder.setStatus(order.getStatus());
        existingOrder.setCustomer(order.getCustomer());
        existingOrder.setAddress(order.getAddress());
        existingOrder.setDeliveryDate(order.getDeliveryDate());

        orderRepository.save(existingOrder);

        return "redirect:/modifyOrder/" + order.getId();
    }

    @PostMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable Long id) {

        orderRepository.deleteById(id);

        return "redirect:/orderList";
    }

}
