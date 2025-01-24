package com.mindhub.order_microservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class NewOrderDto {

    @NotBlank(message = "The email can't be null")
    private String email;

    @NotEmpty(message = "The list can't be null")
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
