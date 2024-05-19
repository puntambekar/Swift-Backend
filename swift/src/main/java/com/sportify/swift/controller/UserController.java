package com.sportify.swift.controller;

import com.sportify.swift.entity.User;
import com.sportify.swift.service.UserService;
import com.sportify.swift.utils.ExtractJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User userRequest) {
        User user = userService.register(userRequest);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } else{  return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);}

    }

    @PostMapping("/details")
    public User getUserDetails(@RequestHeader(value = "Authorization") String token) {
        String email = ExtractJwt.payloadJWTExtraction(token,"\"sub\"");
        User user = userService.getUserDetails(email);
        return user;
    }
}