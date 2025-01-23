package com.mindhub.order_microservice.dtos;

import java.util.List;

public class NewOrderDto {

    private String email;

    private List<OrderItemDtoInput> orderItems;

    public NewOrderDto(String email, List<OrderItemDtoInput> orderItems) {
        this.email = email;
        this.orderItems = orderItems;
    }

    public String getEmail() {
        return email;
    }

    public List<OrderItemDtoInput> getOrderItems() {
        return orderItems;
    }
}
