package com.mindhub.order_microservice.exceptions;

public class InvalidCredentialsExc extends RuntimeException {
    public InvalidCredentialsExc(String message) {
        super(message);
    }
}
