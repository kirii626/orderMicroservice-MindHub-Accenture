package com.mindhub.order_microservice.services.implementations;

import com.mindhub.order_microservice.dtos.NewOrderDto;
import com.mindhub.order_microservice.dtos.OrderItemDtoInput;
import com.mindhub.order_microservice.exceptions.OrderNotFoundExc;
import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.dtos.OrderDtoOutput;
import com.mindhub.order_microservice.models.OrderItemEntity;
import com.mindhub.order_microservice.models.enums.OrderStatus;
import com.mindhub.order_microservice.publishers.OrderEventPublisher;
import com.mindhub.order_microservice.publishers.StockEventPublisher;
import com.mindhub.order_microservice.services.OrderService;
import com.mindhub.order_microservice.utils.ProductServiceClient;
import com.mindhub.order_microservice.utils.UserServiceClient;
import com.mindhub.order_microservice.services.mappers.OrderItemMapper;
import com.mindhub.order_microservice.services.mappers.OrderMapper;
import com.mindhub.order_microservice.repositories.OrderRepository;
import com.mindhub.order_microservice.services.validations.ValidOrderFields;
import com.mindhub.order_microservice.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductServiceClient productServiceClient;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private ValidOrderFields validOrderFields;

    @Autowired
    private StockEventPublisher stockEventPublisher;

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<OrderDtoOutput>> createOrder(NewOrderDto newOrderDto) {
        // Validate user exists
        Long userId = validOrderFields.validateUser(newOrderDto.getEmail());

        // Validate stock products
        validOrderFields.validateProductsStock(newOrderDto.getOrderItems());

        // Create orderEntity and associate orderItems
        OrderEntity orderEntity = createOrderEntity(userId, newOrderDto);

        // Reduce stock products
        stockEventPublisher.sendReduceStockEvent(newOrderDto.getOrderItems(), orderEntity.getId());

        // Map to Dto output
        OrderDtoOutput orderDtoOutput = orderMapper.toOrderDto(orderEntity);

        orderEventPublisher.sendOrderCreatedEvent(orderDtoOutput);

        // Build the response
        ApiResponse<OrderDtoOutput> response = new ApiResponse<>(
                "Order created successfully",
                orderDtoOutput
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    private OrderEntity createOrderEntity(Long userId, NewOrderDto newOrderDto) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(userId);
        orderEntity.setOrderStatus(OrderStatus.PENDING);

        Set<OrderItemEntity> orderItems = newOrderDto.getOrderItems()
                .stream()
                .map(item -> orderItemMapper.toOrderItemEntity(item, orderEntity))
                .collect(Collectors.toSet());
        orderEntity.setOrderItems(orderItems);

        return orderRepository.save(orderEntity);
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
    @Transactional
    public ResponseEntity<ApiResponse<OrderDtoOutput>> updateOrderStatus(Long id, OrderStatus status) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundExc("Order not found with ID: " + id));

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
