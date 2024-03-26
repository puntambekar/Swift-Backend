package com.sportify.swift.controller;

import com.sportify.swift.entity.Venue;
import com.sportify.swift.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping("/venues")
    public List<Venue> getAllVenues(){
        return venueService.getAllVenues();
    }

    @GetMapping("/venues/details")
    public Venue getAVenue(@RequestParam String venueId){
        return venueService.getAVenue(venueId);
    }


    @GetMapping("/venues/list")
    public List<String> getAllVenuesinList(){
       return venueService.getAllVenuesinList();
    }
//
//    @PostMapping(value = "/venues/list/create")
//    public HttpStatus createNewVenueList(@RequestBody List<String> venues){
//        venueService.createNewVenueList(venues);
//        return HttpStatus.CREATED;
//    }
//
//    @PutMapping("/venues/list/add")
//    public HttpStatus addNewVenue(@RequestParam String venueName){
//        venueService.addNewVenueinList(venueName);
//        return HttpStatus.CREATED;
//    }

}
