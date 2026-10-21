package harjoitus.deliverytracking.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "orderTable")

public class Order {
  public enum OrderStatus {
    UNASSIGNED,
    ASSIGNED,
    IN_PROGRESS,
    DELIVERED,
    FAILED_DELIVERY,
    CANCELLED
  }

  // Tarkistaa statuksen ja kuljettajan yhteensopivuuden
  @AssertTrue(message = "Invalid status and driver combination")
  public boolean isValidStatusAndDriver() {

    if (status == null) {
      return false;
    }

    if (status == OrderStatus.UNASSIGNED) {
      return user == null;
    }

    if (status == OrderStatus.CANCELLED) {
      return true;
    }

    return user != null;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id", nullable = false, updatable = false)
  private Long id;

  @NotBlank(message = "Customer is required")
  @Size(min = 2, max = 100)
  @Column(name = "customer", nullable = false)
  private String customer;

  @NotBlank(message = "Address is required")
  @Size(min = 5, max = 200)
  @Column(name = "address", nullable = false)
  private String address;

  // @FutureOrPresent(message = "Delivery date cannot be in the past") | Ei
  // käytetä kaatuu, jos menneisyydessä oleva päivämäärä
  @NotNull(message = "Delivery date is required")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Column(name = "delivery_date", nullable = false)
  private LocalDate deliveryDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private OrderStatus status = OrderStatus.UNASSIGNED;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private AppUser user;

  // Tätä ei nyt käytetä, jatkokehitystä varten mikäli halutaan voida järjestää
  // tilauksia toimitusjärjestyksen mukaan.
  @Column(name = "delivery_order")
  private Integer deliveryOrder;

  // Toimituksen valmistumisaika
  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  public Order() {
  }

  public Order(String customer, String address, LocalDate deliveryDate) {
    this.customer = customer;
    this.address = address;
    this.deliveryDate = deliveryDate;
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

  public Integer getDeliveryOrder() {
    return deliveryOrder;
  }

  public void setDeliveryOrder(Integer deliveryOrder) {
    this.deliveryOrder = deliveryOrder;
  }

  public LocalDateTime getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(LocalDateTime completedAt) {
    this.completedAt = completedAt;
  }
}