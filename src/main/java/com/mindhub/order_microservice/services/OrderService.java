package com.mindhub.order_microservice.services;

import com.mindhub.order_microservice.dtos.NewOrderDto;
import com.mindhub.order_microservice.dtos.OrderDtoInput;
import com.mindhub.order_microservice.dtos.OrderDtoOutput;
import com.mindhub.order_microservice.models.enums.OrderStatus;
import com.mindhub.order_microservice.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    ResponseEntity<ApiResponse<OrderDtoOutput>> createOrder(NewOrderDto newOrderDto);

    ResponseEntity<ApiResponse<List<OrderDtoOutput>>> getAllOrders();

    ResponseEntity<ApiResponse<OrderDtoOutput>> updateOrderStatus(Long id, OrderStatus status);

}
