package com.mindhub.order_microservice.dtos;

import com.mindhub.order_microservice.models.OrderEntity;

public class OrderItemDtoOutput {

    private Long id;
    private Long orderId;
    private Long productId;
    private String name;
    private Integer quantity;
    private Double price;
    private Double subtotal; // quantity * price

    public OrderItemDtoOutput(Long id, Long orderId, Long productId, String name, Integer quantity, Double price) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = price * quantity;
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

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Double getSubtotal() {
        return subtotal;
    }
}
