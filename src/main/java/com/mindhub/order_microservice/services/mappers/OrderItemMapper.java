package com.mindhub.order_microservice.services.mappers;

import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.OrderItemEntity;
import com.mindhub.order_microservice.dtos.OrderItemDtoInput;
import com.mindhub.order_microservice.dtos.OrderItemDtoOutput;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper {

    public OrderItemEntity toOrderItemEntity(OrderItemDtoInput orderItemDtoInput, OrderEntity orderEntity) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setProductId(orderItemDtoInput.getProductId());
        orderItemEntity.setQuantity(orderItemDtoInput.getQuantity());

        orderItemEntity.setOrder(orderEntity);

        return orderItemEntity;
    }

    public OrderItemEntity toOrderItemEntity(OrderItemDtoInput orderItemDtoInput) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setProductId(orderItemDtoInput.getProductId());
        orderItemEntity.setQuantity(orderItemDtoInput.getQuantity());

        return orderItemEntity;
    }

    public OrderItemDtoOutput toOrderItemDto(OrderItemEntity orderItemEntity) {
        return new OrderItemDtoOutput(
                orderItemEntity.getId(),
                orderItemEntity.getOrder() != null ? orderItemEntity.getOrder().getId() : null,
                orderItemEntity.getProductId(),
                orderItemEntity.getQuantity()
        );
    }

    public List<OrderItemDtoOutput> toOrderItemDtoList(List<OrderItemEntity> orderItemEntityList) {
        return orderItemEntityList
                .stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toList());
    }
}
