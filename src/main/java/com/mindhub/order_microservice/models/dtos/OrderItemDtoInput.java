package com.mindhub.order_microservice.models.dtos;

import com.mindhub.order_microservice.models.OrderEntity;

public class OrderItemDtoInput {

    private OrderEntity order;

    private Long productId;

    private Integer quantity;

    public OrderItemDtoInput() {
    }

    public OrderItemDtoInput(OrderEntity order, Long productId, Integer quantity) {
        this.order = order;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
