package com.sportify.swift.controller;

import com.sportify.swift.entity.Availability;
import com.sportify.swift.entity.Venue;
import com.sportify.swift.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api")
public class VenueController {
    @Autowired
    private VenueService venueService;

    @PostMapping("/add/venue")
    public HttpStatus addVenue(@RequestBody Venue addVenue) {
        venueService.addNewVenue(addVenue);
        return HttpStatus.CREATED;
    }

    @GetMapping("/venues/details")
    public Venue getVenueDetails(){
        return venueService.getVenueDetails();
    }


 @GetMapping("/venues/dailyAvail")
    public List<Availability.DailyAvailability.HourlyAvailability>  getVenueAvailabilityForDay(String venueId, String date){
        return venueService.getVenueAvailabilityForDay(venueId,date);
    }

}
