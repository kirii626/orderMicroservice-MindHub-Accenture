package com.mindhub.order_microservice.dtos;

import com.mindhub.order_microservice.models.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderDtoInput {

    @NotNull(message = "The user ID can´t be null")
    private Long userId;

    @NotNull(message = "The list of order items can't be null")
    private List<OrderItemDtoInput> orderItems;

    @NotNull(message = "The order status can't be null")
    private OrderStatus orderStatus;

    public OrderDtoInput() {
    }

    public OrderDtoInput(Long userId, List<OrderItemDtoInput> orderItems, OrderStatus orderStatus) {
        this.userId = userId;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderItemDtoInput> getOrderItems() {
        return orderItems;
    }
}
