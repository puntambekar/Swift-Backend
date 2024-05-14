package com.sportify.swift.service;

import com.sportify.swift.dao.UserRepository;
import com.sportify.swift.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Value("${okta.user.api}")
    String okta_user_api;

    @Value("${api.token}")
    String api_token;

    @Autowired
    UserRepository userRepository;
    public User register(User userRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "SSWS "+api_token);

        String profileJson = String.format("{\"profile\": {\"firstName\": \"%s\", \"lastName\": \"%s\", \"email\": \"%s\", \"login\": \"%s\"}}",
                userRequest.getFirstName(), userRequest.getLastName(), userRequest.getEmail(), userRequest.getEmail());

        HttpEntity<String> requestEntity = new HttpEntity<>(profileJson,headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> responseEntity = restTemplate.exchange(okta_user_api, HttpMethod.POST, requestEntity, User.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("User created successfully");
            userRepository.save(userRequest);
            return  userRequest;

        } else {
            System.out.println("Failed to create user: " + responseEntity.getBody());
            return null;
        }
        
    }
}
