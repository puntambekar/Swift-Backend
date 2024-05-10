package com.sportify.swift.controller;


import com.sportify.swift.entity.Booking;
import com.sportify.swift.requestmodel.BookingRequest;
import com.sportify.swift.responsemodel.BookingEventResponse;
import com.sportify.swift.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping("/add")
    public ResponseEntity<Void> addBooking(@RequestBody BookingRequest bookingRequest) {
        bookingService.addNewBooking(bookingRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelBooking(@RequestBody String bookingId) throws Exception {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/slots")
    public List<BookingEventResponse> getAllBookingsBySlots(){
        return bookingService.getAllBookingsBySlots();
    }

    @GetMapping("/list")
    public List<Booking> getAllBookings(){
        return bookingService.getAllBookings();
    }
}
