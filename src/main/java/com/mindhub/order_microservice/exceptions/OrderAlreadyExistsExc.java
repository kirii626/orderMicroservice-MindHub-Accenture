package com.mindhub.order_microservice.exceptions;

public class OrderAlreadyExistsExc extends RuntimeException {
    public OrderAlreadyExistsExc(String message) {
        super(message);
    }
}
