package harjoitus.deliverytracking.web;

import harjoitus.deliverytracking.repository.AppUserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
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

// http://localhost:8080/orders

@Controller
public class OrderController {

    private final AppUserRepository appUserRepository;
    private final OrderRepository orderRepository;
    private final Validator validator;

    public OrderController(OrderRepository orderRepository, AppUserRepository appUserRepository, Validator validator) {
        this.validator = validator;
        this.orderRepository = orderRepository;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {

        LocalDate today = LocalDate.now();
        model.addAttribute("today", today);

        // Yksi tyhjä Order-olio lomaketta varten
        Order order = new Order();
        order.setDeliveryDate(today);
        model.addAttribute("order", order);

        model.addAttribute("orders", orderRepository.findAll());
        model.addAttribute("users", appUserRepository.findAll());

        return "orders";
    }

    @PostMapping("/orders")
    public String addOrder(
            @Valid @ModelAttribute("order") Order order,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("orders", orderRepository.findAll());
            model.addAttribute("users", appUserRepository.findAll());
            model.addAttribute("today", LocalDate.now());
            return "orders";
        }

        orderRepository.save(order);

        return "redirect:/orders";
    }

    // Tästä alkaa olemassa olevien tilausten muokkaukset:
    @GetMapping("/editOrder/{id}")
    public String editOrder(@PathVariable Long id, Model model) {

        // model.addAttribute("users", appUserRepository.findAll());
        // Vaihdettu vain käyttäjiin:
        model.addAttribute("users", appUserRepository.findByRole("USER"));
        Order order = orderRepository.findById(id).orElseThrow();
        model.addAttribute("order", order);

        return "edit-order";
    }

    // Päivittää olemassa olevan tilauksen perustiedot
    @PostMapping("/updateOrder")
    public String updateOrder(
            @Valid @ModelAttribute("order") Order order,
            BindingResult result,
            Model model) {

        // Tarkistetaan validointivirheet
        if (result.hasErrors()) {
            model.addAttribute("users", appUserRepository.findAll());
            return "edit-order";
        }

        Order existingOrder = orderRepository.findById(order.getId())
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        // Päivitetään perustiedot
        existingOrder.setCustomer(order.getCustomer());
        existingOrder.setAddress(order.getAddress());
        existingOrder.setDeliveryDate(order.getDeliveryDate());

        orderRepository.save(existingOrder);

        return "redirect:/editOrder/" + order.getId();
    }

    // Päivittää olemassa olevan tilauksen statuksen, kuljettajan ja valmistumisajan
    @PostMapping("/updateDelivery")
    public String updateDelivery(
            @RequestParam Long orderId,
            @RequestParam Order.OrderStatus status,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime completedAt,
            Model model) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + orderId));

        // Päivitetään status ja kuljettaja
        order.setStatus(status);

        AppUser user = null;

        if (userId != null) {
            user = appUserRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        }

        order.setUser(user);

        // Asetetaan valmistumisaika validointia varten
        order.setCompletedAt(completedAt);

        // Tarkistetaan @AssertTrue-validointi
        var errors = validator.validateProperty(order, "validStatusAndDriver");

        if (!errors.isEmpty()) {
            model.addAttribute("order", order);
            model.addAttribute("users", appUserRepository.findByRole("USER"));
            model.addAttribute("completedAtInput", completedAt);
            model.addAttribute("deliveryError", "Invalid status and driver combination");

            return "edit-order";
        }

        // Tarkistetaan @PastOrPresent-validointi
        var timeErrors = validator.validateProperty(order, "completedAt");

        if (!timeErrors.isEmpty()) {
            model.addAttribute("order", order);
            model.addAttribute("users", appUserRepository.findByRole("USER"));
            model.addAttribute("completedAtInput", completedAt);
            model.addAttribute("deliveryError", "Completion time cannot be in the future");

            return "edit-order";
        }

        // Valmistumisaika sallitaan vain valmiille toimituksille
        if (completedAt != null &&
                status != Order.OrderStatus.DELIVERED &&
                status != Order.OrderStatus.FAILED_DELIVERY) {

            model.addAttribute("order", order);
            model.addAttribute("users", appUserRepository.findByRole("USER"));
            model.addAttribute("completedAtInput", completedAt);
            model.addAttribute("deliveryError", "Completion time is only allowed for completed deliveries");

            return "edit-order";
        }

        // Tallennetaan status, kuljettaja ja valmistumisaika
        orderRepository.save(order);

        return "redirect:/editOrder/" + orderId;
    }

    // Lisätty harjoituksena yksi metoditasoinen suojaus
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable Long id) {

        orderRepository.deleteById(id);

        return "redirect:/orders";
    }

    // Tilausten asettaminen kuljettajille | Assign Drivers:
    @GetMapping("/assignDrivers")
    public String showAssignDrivers(Model model) {

        model.addAttribute("orders",
                orderRepository.findByStatus(Order.OrderStatus.UNASSIGNED));

        model.addAttribute("users", appUserRepository.findByRole("USER"));

        return "assign-drivers";
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
