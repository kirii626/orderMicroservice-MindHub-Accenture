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

    private Long userId;

    @OneToMany
    private Set<OrderItemEntity> orderItems = new HashSet<>();

    private OrderStatus orderStatus;

    public OrderEntity() {
    }

    public OrderEntity(Long userId, Set<OrderItemEntity> orderItems, OrderStatus orderStatus) {
        this.userId = userId;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
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
}
