package com.mindhub.order_microservice.publishers;

import com.mindhub.order_microservice.dtos.OrderDtoOutput;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey.order}")
    private String orderRoutingKey;

    public void sendOrderCreatedEvent(OrderDtoOutput orderDto) {
        rabbitTemplate.convertAndSend(exchange, orderRoutingKey, orderDto);
    }
}
