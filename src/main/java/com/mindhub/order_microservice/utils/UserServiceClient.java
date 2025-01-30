package com.mindhub.order_microservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    public Long getUserIdByEmail(String email) {
        String url = "http://user-microservice/internal/user/by-email?email=" + email;
        ResponseEntity<Long> response = restTemplate.getForEntity(url, Long.class);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to retrieve user ID for email: " + email);
        }
        return response.getBody();
    }
}
