package com.mindhub.order_microservice.models.dtos;

import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;

public class OrderDtoOutput {

    private Long id;

    private OrderEntity order;

    private Long userId;

    private OrderStatus orderStatus;

    public OrderDtoOutput(OrderEntity order, Long userId, OrderStatus orderStatus) {
        this.order = order;
        this.userId = userId;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public Long getUserId() {
        return userId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
