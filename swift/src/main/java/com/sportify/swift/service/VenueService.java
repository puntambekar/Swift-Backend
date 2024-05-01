package com.sportify.swift.service;

import com.mongodb.client.*;
import com.sportify.swift.dao.VenueRepository;
import com.sportify.swift.entity.Availability;
import com.sportify.swift.entity.Venue;
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


    @Value("${spring.data.mongodb.uri}")
    String mongoConnectionString;

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
        availability.setMonth(5);

        List<Availability.DailyAvailability> dailyAvailabilities = new ArrayList<>();

        LocalDate startDate = LocalDate.parse("2024-05-01");

        LocalDate endDate = startDate.plusDays(30);

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


    public Venue getVenueDetails() {
        List<Venue> venues = venueRepository.findAll();
        if (!venues.isEmpty()) {
            return venues.get(0); // Return the first venue if available
        } else {
            return null; // Return null if no venue found
        }
    }

    //get availability of a venue on a particular day
    public List<Availability.DailyAvailability.HourlyAvailability> getVenueAvailabilityForDay(String venueId, String dateString){


        LocalDate localDate = LocalDate.parse(dateString);
        Venue venue= venueRepository.findById(venueId).get();

        Optional<Availability.DailyAvailability> data = venue.getAvailability().getDailyAvailability().stream().filter(e -> e.getDate() .isEqual(localDate) ).findFirst();


        return data.get().getHourlyAvailability();
    }

    public void save(Venue venue) {
        venueRepository.save(venue);
    }
}
