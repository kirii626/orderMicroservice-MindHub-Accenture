package com.mindhub.order_microservice.exceptions;

public class InsufficientStockExc extends RuntimeException {
        private final Long productId;
        private final Integer requestedQuantity;

        public InsufficientStockExc(Long productId, Integer requestedQuantity) {
            super("Product ID: " + productId + " has insufficient stock.");
            this.productId = productId;
            this.requestedQuantity = requestedQuantity;
        }

        public Long getProductId() {
            return productId;
        }

        public Integer getRequestedQuantity() {
            return requestedQuantity;
        }

}

