package com.sportify.swift.service;

import com.mongodb.client.*;
import com.sportify.swift.dao.VenueRepository;
import com.sportify.swift.entity.Availability;
import com.sportify.swift.entity.Venue;
import com.sportify.swift.responsemodel.VenueResponse;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VenueService {

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    AvailabilityService availabilityService;


    public void addNewVenue(Venue newVenue) {
        Venue venue = new Venue();
        venue.setBusinessName(newVenue.getBusinessName());
        venue.setAddress(newVenue.getAddress());
        venue.setCity(newVenue.getCity());
       // venue.setAvailability(createDummyAvailabilityData());
        venueRepository.save(venue);
    }



    public VenueResponse getVenueDetails() {
        Venue venue = venueRepository.findAll().get(0);
        VenueResponse venueResponse = new VenueResponse();
        venueResponse.setId(venue.getId());
        venueResponse.setBusinessName(venue.getBusinessName());
        venueResponse.setAddress(venue.getAddress());
        venueResponse.setCity(venue.getCity());

        List<Availability> availabilityDataForThreeMonths = availabilityService.getAvailabilityDataForThreeMonths();

        venueResponse.setAvailability(availabilityDataForThreeMonths);
        return venueResponse;

    }

    //get availability of a venue on a particular day
//    public List<Availability.DailyAvailability.HourlyAvailability> getVenueAvailabilityForDay(String venueId, String dateString){
//
//
//        LocalDate localDate = LocalDate.parse(dateString);
//        Venue venue= venueRepository.findById(venueId).get();
//
//        Optional<Availability.DailyAvailability> data = venue.getAvailability().getDailyAvailability().stream().filter(e -> e.getDate() .isEqual(localDate) ).findFirst();
//
//
//        return data.get().getHourlyAvailability();
//    }

    public void save(Venue venue) {
        venueRepository.save(venue);
    }
}
