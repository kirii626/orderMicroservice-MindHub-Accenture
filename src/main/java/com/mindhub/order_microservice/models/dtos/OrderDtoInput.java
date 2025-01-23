package com.mindhub.order_microservice.models.dtos;

import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;

public class OrderDtoInput {

    @NotBlank(message = "The user ID can´t be null")
    private Long userId;

    @NotBlank(message = "The order ID can't be null")
    private OrderEntity order;

    @NotBlank(message = "The order status can't be null")
    private OrderStatus orderStatus;

    public OrderDtoInput() {
    }

    public OrderDtoInput(Long userId, OrderEntity order, OrderStatus orderStatus) {
        this.userId = userId;
        this.order = order;
        this.orderStatus = orderStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderEntity getOrder() {
        return order;
    }
}
