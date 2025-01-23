package com.mindhub.order_microservice.dtos;

import com.mindhub.order_microservice.models.OrderEntity;
import jakarta.validation.constraints.NotBlank;

public class OrderItemDtoInput {

    @NotBlank(message = "The order ID can't be null")
    private Long orderId;

    @NotBlank(message = "The product ID can't be null")
    private Long productId;

    @NotBlank(message = "The quantity can't be null")
    private Integer quantity;

    public OrderItemDtoInput() {
    }

    public OrderItemDtoInput(Long orderId, Long productId, Integer quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
