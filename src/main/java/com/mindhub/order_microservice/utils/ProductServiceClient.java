package com.mindhub.order_microservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    public boolean validateStock(Long productId, Integer quantity) {
        String url = "http://product-microservice/products/" + productId + "/validate-stock?quantity=" + quantity;
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
        return response.getBody() != null && response.getBody();
    }

    public void reduceStock(Long productId, Integer quantity) {
        String url = "http://product-microservice/products/" + productId + "/reduce-stock";
        restTemplate.postForEntity(url, quantity, Void.class);
    }

}
