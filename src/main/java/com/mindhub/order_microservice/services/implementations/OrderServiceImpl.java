package com.mindhub.order_microservice.services.implementations;

import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.dtos.OrderDtoInput;
import com.mindhub.order_microservice.dtos.OrderDtoOutput;
import com.mindhub.order_microservice.models.enums.OrderStatus;
import com.mindhub.order_microservice.services.OrderService;
import com.mindhub.order_microservice.services.mappers.OrderMapper;
import com.mindhub.order_microservice.repositories.OrderRepository;
import com.mindhub.order_microservice.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public ResponseEntity<ApiResponse<OrderDtoOutput>> createOrder(OrderDtoInput orderDtoInput) {
        OrderEntity orderEntity = orderMapper.toOrderEntity(orderDtoInput);
        OrderEntity savedOrder = orderRepository.save(orderEntity);
        OrderDtoOutput orderDtoOutput = orderMapper.toOrderDto(savedOrder);

        ApiResponse<OrderDtoOutput> response = new ApiResponse<>(
                "Order created successfully",
                orderDtoOutput
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<ApiResponse<List<OrderDtoOutput>>> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAll();

        List<OrderDtoOutput> orderDtoOutputs = orderMapper.toOrderDtoList(orders);

        ApiResponse<List<OrderDtoOutput>> response = new ApiResponse<>(
                "List of all orders",
                orderDtoOutputs
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponse<OrderDtoOutput>> updateOrderStatus(Long id, OrderStatus status) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));

        orderEntity.setOrderStatus(status);

        OrderEntity updatedOrder = orderRepository.save(orderEntity);

        OrderDtoOutput orderDtoOutput = orderMapper.toOrderDto(updatedOrder);

        ApiResponse<OrderDtoOutput> response = new ApiResponse<>(
                "Order status updated successfully",
                orderDtoOutput
        );

        return ResponseEntity.ok(response);
    }


}
