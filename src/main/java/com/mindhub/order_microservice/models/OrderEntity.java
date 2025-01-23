package com.mindhub.order_microservice.models;

import com.mindhub.order_microservice.models.enums.OrderStatus;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItemEntity> orderItems = new HashSet<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    public OrderEntity() {
    }

    public OrderEntity(Long userId, OrderStatus orderStatus) {
        this.userId = userId;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void addOrderItem(OrderItemEntity item) {
        item.setOrder(this); // Bidirectional relationship
        this.orderItems.add(item);
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderItems=" + orderItems +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
