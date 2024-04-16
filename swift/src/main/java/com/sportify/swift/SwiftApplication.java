package com.sportify.swift;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportify.swift.entity.Availability;
import com.sportify.swift.entity.Venue;
import com.sportify.swift.entity.VenueList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SwiftApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwiftApplication.class, args);
    }
}
