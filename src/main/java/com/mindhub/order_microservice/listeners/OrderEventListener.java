package com.mindhub.order_microservice.listeners;

import com.mindhub.order_microservice.exceptions.OrderNotFoundExc;
import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.enums.OrderStatus;
import com.mindhub.order_microservice.repositories.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class OrderEventListener {

    private static final Logger logger = Logger.getLogger(OrderEventListener.class.getName());

    @Autowired
    private OrderRepository orderRepository;

    @RabbitListener(queues = "${rabbitmq.queue.stock.success}")
    public void handleStockSuccess(Long orderId) {
        logger.info("Processing stock confirmation for Order ID: " + orderId);

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.warning("Order not found: " + orderId);
                    return new OrderNotFoundExc("Order not found with ID: " + orderId);
                });

        order.setOrderStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        logger.info("Order ID: " + orderId + " successfully updated to status: " + OrderStatus.COMPLETED);
    }

    @RabbitListener(queues = "${rabbitmq.queue.stock.failure}")
    public void handleStockFailure(Long orderId) {
        logger.info("Processing stock failure for Order ID: " + orderId);

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.warning("Order not found: " + orderId);
                    return new OrderNotFoundExc("Order not found with ID: " + orderId);
                });

        order.setOrderStatus(OrderStatus.FAILED);
        orderRepository.save(order);

        logger.warning("Order ID: " + orderId + " updated to status: " + OrderStatus.FAILED);
    }
}
