package com.mindhub.order_microservice.publishers;

import com.mindhub.order_microservice.dtos.OrderItemDtoInput;
import com.mindhub.order_microservice.events.StockReduceEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String stockExchange;

    @Value("${rabbitmq.routing.stock.reduce}")
    private String stockReduceRoutingKey;

    public void sendReduceStockEvent(List<OrderItemDtoInput> orderItems, Long orderId) {
        StockReduceEvent event = new StockReduceEvent(orderItems, orderId);
        rabbitTemplate.convertAndSend(stockExchange, stockReduceRoutingKey, event);
    }
}
