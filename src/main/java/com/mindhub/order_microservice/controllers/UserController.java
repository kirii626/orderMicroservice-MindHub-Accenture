package com.mindhub.order_microservice.controllers;

import com.mindhub.order_microservice.dtos.NewOrderDto;
import com.mindhub.order_microservice.dtos.OrderDtoOutput;
import com.mindhub.order_microservice.services.OrderService;
import com.mindhub.order_microservice.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-user")
public class UserController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDtoOutput>> createOrder(
            @Valid
            @RequestBody NewOrderDto newOrderDto) {
        return orderService.createOrder(newOrderDto);
    }


}
