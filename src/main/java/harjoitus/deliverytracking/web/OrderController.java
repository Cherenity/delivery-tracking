package harjoitus.deliverytracking.web;

import harjoitus.deliverytracking.repository.AppUserRepository;

import java.time.LocalDate;
import java.util.List;
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
import jakarta.validation.Validator;

import org.springframework.web.bind.annotation.RequestParam;

// http://localhost:8080/orderList

@Controller
public class OrderController {

    private final AppUserRepository appUserRepository;
    private final OrderRepository orderRepository;
    private final Validator validator;

    public OrderController(OrderRepository orderRepository, AppUserRepository appUserRepository,  Validator validator) {
        this.validator = validator;
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
        model.addAttribute("today", today);
        model.addAttribute("users", appUserRepository.findAll());

        return "orderList";
    }

    @PostMapping("/orderList")
    public String addOrder(@Valid @ModelAttribute("order") Order order, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("orders", orderRepository.findAll());
            model.addAttribute("users", appUserRepository.findAll());
            model.addAttribute("today", LocalDate.now());
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

    @GetMapping("/modifyOrder/{id}")
    public String modifyOrder(@PathVariable Long id, Model model) {
        model.addAttribute("users", appUserRepository.findAll());

        Order order = orderRepository.findById(id).orElseThrow();

        model.addAttribute("order", order);

        return "modifyOrder";
    }

    // Päivittää tilauksen perustiedot
    @PostMapping("/updateOrder")
    public String updateOrder(
            @Valid @ModelAttribute("order") Order order,
            BindingResult result,
            Model model) {

        // Tarkistetaan validointivirheet
        if (result.hasErrors()) {
            model.addAttribute("users", appUserRepository.findAll());
            return "modifyOrder";
        }

        Order existingOrder = orderRepository.findById(order.getId()).orElseThrow();

        // Päivitetään perustiedot
        existingOrder.setCustomer(order.getCustomer());
        existingOrder.setAddress(order.getAddress());
        existingOrder.setDeliveryDate(order.getDeliveryDate());

        orderRepository.save(existingOrder);

        return "redirect:/modifyOrder/" + order.getId();
    }

    // Päivittää tilauksen statuksen ja kuljettajan
    @PostMapping("/updateDelivery")
    public String updateDelivery(
            @RequestParam Long orderId,
            @RequestParam Order.OrderStatus status,
            @RequestParam(required = false) Long userId,
            Model model) {

        Order order = orderRepository.findById(orderId).orElseThrow();

        // Päivitetään status ja kuljettaja
        order.setStatus(status);

        AppUser user = null;
        if (userId != null) {
            user = appUserRepository.findById(userId).orElseThrow();
        }
        order.setUser(user);

        // Tarkistetaan @AssertTrue-validointi
        var errors = validator.validateProperty(order, "validStatusAndDriver");

        if (!errors.isEmpty()) {
            model.addAttribute("order", order);
            model.addAttribute("users", appUserRepository.findAll());
            model.addAttribute("deliveryError", "Invalid status and driver combination");

            return "modifyOrder";
        }

        orderRepository.save(order);

        return "redirect:/modifyOrder/" + orderId;
    }

    @PostMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable Long id) {

        orderRepository.deleteById(id);

        return "redirect:/orderList";
    }

    // KULJETTAJIEN MÄÄRITYKSET (SAA LISÄTÄ USEAMPIA)

    /// Näyttää kuljettajien määrityssivun
    @GetMapping("/assignDrivers")
    public String showAssignDrivers(Model model) {

        model.addAttribute("orders",
                orderRepository.findByStatus(Order.OrderStatus.UNASSIGNED));

        model.addAttribute("users", appUserRepository.findAll());

        return "assignDrivers";
    }

    // Määrittää useita tilauksia yhdelle kuljettajalle
    @PostMapping("/assignMultiple")
    public String assignMultiple(
            @RequestParam(required = false) List<Long> orderIds,
            @RequestParam Long userId) {

        // Tarkistetaan, että tilauksia on valittu
        if (orderIds == null || orderIds.isEmpty()) {
            throw new IllegalArgumentException("No orders selected");
        }

        AppUser user = appUserRepository.findById(userId).orElseThrow();

        for (Long orderId : orderIds) {

            Order order = orderRepository.findById(orderId).orElseThrow();

            // Vain määrittämättömät tilaukset voidaan valita
            if (order.getStatus() != Order.OrderStatus.UNASSIGNED) {
                throw new IllegalArgumentException("Order is already assigned");
            }

            // Päivitetään kuljettaja ja status
            order.setUser(user);
            order.setStatus(Order.OrderStatus.ASSIGNED);

            orderRepository.save(order);
        }

        return "redirect:/assignDrivers";
    }

}
