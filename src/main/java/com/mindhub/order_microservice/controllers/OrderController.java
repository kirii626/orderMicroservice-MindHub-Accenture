package com.mindhub.order_microservice.controllers;

import com.mindhub.order_microservice.dtos.OrderDtoInput;
import com.mindhub.order_microservice.dtos.OrderDtoOutput;
import com.mindhub.order_microservice.models.enums.OrderStatus;
import com.mindhub.order_microservice.services.OrderService;
import com.mindhub.order_microservice.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDtoOutput>> createOrder(@Valid @RequestBody OrderDtoInput orderDtoInput) {
        return orderService.createOrder(orderDtoInput);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDtoOutput>>> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<ApiResponse<OrderDtoOutput>> updateOrderStatus(
            @Valid
            @PathVariable Long id,
            @RequestParam("status") OrderStatus status) {
        return orderService.updateOrderStatus(id, status);
    }

}
