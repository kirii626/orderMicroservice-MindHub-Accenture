package com.mindhub.order_microservice.exceptions;

public class OrderNotFoundExc extends RuntimeException {
    public OrderNotFoundExc(String message) {
        super(message);
    }
}
