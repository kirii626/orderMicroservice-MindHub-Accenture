package com.mindhub.order_microservice.dtos;

import com.mindhub.order_microservice.models.OrderEntity;

public class OrderItemDtoOutput {

    private Long id;

    private Long orderId;

    private Long productId;

    private Integer quantity;

    public OrderItemDtoOutput(Long id, Long orderId, Long productId, Integer quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
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
