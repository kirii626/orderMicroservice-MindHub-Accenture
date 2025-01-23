package com.mindhub.order_microservice.services.validations;

import com.mindhub.order_microservice.dtos.NewOrderDto;
import com.mindhub.order_microservice.dtos.OrderItemDtoInput;
import com.mindhub.order_microservice.exceptions.InsufficientStockExc;
import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.OrderItemEntity;
import com.mindhub.order_microservice.models.enums.OrderStatus;
import com.mindhub.order_microservice.repositories.OrderRepository;
import com.mindhub.order_microservice.services.ProductServiceClient;
import com.mindhub.order_microservice.services.UserServiceClient;
import com.mindhub.order_microservice.services.mappers.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidOrderFields {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private ProductServiceClient productServiceClient;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderRepository orderRepository;

    public Long validateUser(String email) {
        return userServiceClient.getUserIdByEmail(email);
    }

    public void validateProductsStock(List<OrderItemDtoInput> orderItems) {
        for (OrderItemDtoInput item : orderItems) {
            boolean isValid = productServiceClient.validateStock(item.getProductId(), item.getQuantity());
            if (!isValid) {
                throw new InsufficientStockExc(item.getProductId(), item.getQuantity());
            }
        }
    }

}
