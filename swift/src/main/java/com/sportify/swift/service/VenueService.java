package com.sportify.swift.service;

import com.mongodb.client.*;
import com.sportify.swift.dao.VenueListRepository;
import com.sportify.swift.dao.VenueRepository;
import com.sportify.swift.entity.Availability;
import com.sportify.swift.entity.Venue;
import com.sportify.swift.entity.VenueList;
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
    VenueListRepository venueListRepository;

    @Value("${spring.data.mongodb.uri}")
    String mongoConnectionString;

    public List<String> getAllVenuesinList() {
        List<String> businessNames = new ArrayList<>();
       // return venueListRepository.findById(0).get().getVenueList();
        try(MongoClient mongoClient = MongoClients.create(mongoConnectionString)){
            MongoDatabase db = mongoClient.getDatabase("swift");
            MongoCollection<Document> collection = db.getCollection("venue");
            DistinctIterable<String> distinctIterable = collection.distinct("businessName", String.class);

            for(String businessName:distinctIterable){
                businessNames.add(businessName);
            }
        }
        return businessNames;
    }

//    public List<String> addNewVenueinList(String newVenue) {
//        Optional<VenueList> venueList = venueListRepository.findById(1);
//        venueList.get().getVenueList().add(newVenue);
//
//
//        return venueListRepository.save(venueList.get()).getVenueList();
//    }
//
//    public void createNewVenueList(List<String> venues) {
//        VenueList venueList = new VenueList();
//        venueList.setVenueList(venues);
//        venueListRepository.save(venueList);
//    }

    public void addNewVenue(Venue newVenue) {
        Venue venue = new Venue();
        venue.setBusinessName(newVenue.getBusinessName());
        venue.setAddress(newVenue.getAddress());
        venue.setCity(newVenue.getCity());
        venue.setAvailability(createDummyAvailabilityData());
        venueRepository.save(venue);
    }

    private Availability createDummyAvailabilityData() {

        Availability availability = new Availability();
        availability.setYear(2024);
        availability.setMonth(4);

        List<Availability.DailyAvailability> dailyAvailabilities = new ArrayList<>();

        LocalDate startDate = LocalDate.parse("2024-04-01");

        LocalDate endDate = startDate.plusDays(29);

        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            Availability.DailyAvailability dailyAvailability = new Availability.DailyAvailability();
            dailyAvailability.setDate(date);

            List<Availability.DailyAvailability.HourlyAvailability> hourlyAvailabilities = new ArrayList<>();
            for (int hour = 8; hour <= 16; hour++) {
                hourlyAvailabilities.add(
                        new Availability.DailyAvailability.HourlyAvailability(
                                Date.from(LocalTime.of(hour, 0).atDate(date).atZone(ZoneId.of("America/New_York")).toInstant()), 10));
            }
            dailyAvailability.setHourlyAvailability(hourlyAvailabilities);
            dailyAvailabilities.add(dailyAvailability);
        }

        availability.setDailyAvailability(dailyAvailabilities);
        System.out.println(availability);
        return availability;

    }

    public List<Venue> getAllVenues() {

       return venueRepository.findAll();
    }


    public Venue getAVenue(String venueId) {
        return venueRepository.findById(venueId).get();
    }
}
