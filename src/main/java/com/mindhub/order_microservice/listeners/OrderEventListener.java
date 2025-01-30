package com.mindhub.order_microservice.listeners;

import com.mindhub.order_microservice.exceptions.OrderNotFoundExc;
import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.enums.OrderStatus;
import com.mindhub.order_microservice.repositories.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderEventListener {

    @Autowired
    private OrderRepository orderRepository;

    @RabbitListener(queues = "${rabbitmq.queue.stock.success}")
    public void handleStockSuccess(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundExc("Order not found"));

        order.setOrderStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }

    @RabbitListener(queues = "${rabbitmq.queue.stock.failure}")
    public void handleStockFailure(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundExc("Order not found"));

        order.setOrderStatus(OrderStatus.FAILED);
        orderRepository.save(order);
    }
}
