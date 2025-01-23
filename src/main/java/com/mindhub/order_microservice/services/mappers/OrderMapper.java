package com.mindhub.order_microservice.services.mappers;

import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.OrderItemEntity;
import com.mindhub.order_microservice.dtos.OrderDtoInput;
import com.mindhub.order_microservice.dtos.OrderDtoOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    public OrderDtoOutput toOrderDto(OrderEntity orderEntity) {
        return new OrderDtoOutput(
                orderEntity.getId(),
                orderEntity.getUserId(),
                orderEntity.getOrderStatus(),
                orderEntity.getOrderItems()
                        .stream()
                        .map(orderItemMapper::toOrderItemDto)
                        .collect(Collectors.toList())
        );
    }

    public List<OrderDtoOutput> toOrderDtoList(List<OrderEntity> orderEntityList) {
        return orderEntityList.stream()
                .map(this::toOrderDto)
                .collect(Collectors.toList());
    }

    public OrderEntity toOrderEntity(OrderDtoInput orderDtoInput) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(orderDtoInput.getUserId());
        orderEntity.setOrderStatus(orderDtoInput.getOrderStatus());

        if (orderDtoInput.getOrderItems() != null) {
            Set<OrderItemEntity> orderItems = orderDtoInput.getOrderItems()
                    .stream()
                    .map(itemDto -> orderItemMapper.toOrderItemEntity(itemDto, orderEntity))
                    .collect(Collectors.toSet());
            orderEntity.setOrderItems(orderItems);
        }

        return orderEntity;
    }
}
