package com.sportify.swift.controller;

import com.sportify.swift.entity.Availability;
import com.sportify.swift.requestmodel.AvailabilityRequest;
import com.sportify.swift.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    @Autowired
    AvailabilityService availabilityService;

    @PostMapping("/create")
    public ResponseEntity<Availability> generateAvailabiltyData(@RequestBody AvailabilityRequest request) {

       Availability availability= availabilityService.createDummyAvailabilityData(Integer.parseInt(request.getYear()),Integer.parseInt(request.getMonth()));
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/data")
    public Availability getAvailabilityDataByMonth(String year,String month){
        return  availabilityService.getAvailabilityDataByMonth(year,month);
    }

    @PostMapping("/update")
    public ResponseEntity<Availability> updateAvailabilityData(@RequestBody Availability availability) {

         availabilityService.updateAvailabilityData(availability);
        return ResponseEntity.ok(availability);
    }
}
