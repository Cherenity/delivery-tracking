package harjoitus.deliverytracking.domain;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "orders")

public class Order {
  public enum OrderStatus {
    UNASSIGNED,
    ASSIGNED,
    IN_PROGRESS,
    DELIVERED,
    CANCELLED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Customer is required")
  @Size(min = 2, max = 100)
  private String customer;

  @NotBlank(message = "Address is required")
  @Size(min = 5, max = 200)
  private String address;

  // @FutureOrPresent(message = "Delivery date cannot be in the past") | Ei käytetä kaatuu, jos menneisyydessä oleva päivämäärä
  @NotNull(message = "Delivery date is required")
  private LocalDate deliveryDate;

  
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private AppUser user;

  public Order() {
  }

  public Order(String customer, String address, LocalDate deliveryDate, OrderStatus status) {
    this.customer = customer;
    this.address = address;
    this.deliveryDate = deliveryDate;
    this.status = status;
  }

  public Order(String customer, String address, LocalDate deliveryDate) {
    this.customer = customer;
    this.address = address;
    this.deliveryDate = deliveryDate;
    this.status = OrderStatus.UNASSIGNED;
  }

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public LocalDate getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(LocalDate deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public AppUser getUser() {
    return user;
  }
  
  public void setUser(AppUser user) {
    this.user = user;
  }
}