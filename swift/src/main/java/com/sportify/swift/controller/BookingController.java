package com.sportify.swift.controller;


import com.sportify.swift.entity.Booking;
import com.sportify.swift.requestmodel.BookingRequest;
import com.sportify.swift.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping("/add/booking")
    public ResponseEntity<Void> addBooking(@RequestBody BookingRequest bookingRequest) {
        bookingService.addNewBooking(bookingRequest);
        return ResponseEntity.ok().build();
    }
}
