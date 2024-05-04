package com.sportify.swift.service;

import com.sportify.swift.dao.BookingRepository;
import com.sportify.swift.entity.Availability;
import com.sportify.swift.entity.Booking;
import com.sportify.swift.entity.Venue;
import com.sportify.swift.requestmodel.BookingRequest;
import com.sportify.swift.requestmodel.TimeSlot;
import com.sportify.swift.responsemodel.BookingEventResponse;
import com.sportify.swift.utils.Constants;
import com.sportify.swift.utils.DateUtils;
import com.sportify.swift.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    VenueService venueService;

    @Autowired
    EmailUtils emailUtils;


    public void addNewBooking(BookingRequest bookingRequest) {

        LocalDate localDate = LocalDate.parse(bookingRequest.getDate());


        Venue venue = venueService.getVenueDetails();

        if (venue != null) {
            Optional<Availability.DailyAvailability> data = venue.getAvailability().getDailyAvailability().stream()
                    .filter(e -> e.getDate().isEqual(localDate))
                    .findFirst();

            data.ifPresent(dailyAvailability -> {
                for (TimeSlot timeSlot : bookingRequest.getTimeSlots()) {
                    Optional<Availability.DailyAvailability.HourlyAvailability> hourlyAvailability = dailyAvailability.getHourlyAvailability().stream()
                            .filter(item -> {
                                try {
                                    return item.getTime().equals(DateUtils.dateFormatter(timeSlot.getTime()));
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .findFirst();

                    hourlyAvailability.ifPresent(availability -> {
                        availability.setCourtAvailable(availability.getCourtAvailable() - Integer.parseInt(timeSlot.getCourtBooked()));
                    });
                }
            });

            venueService.save(venue);
        }

        Booking booking = new Booking();
        booking.setDate(bookingRequest.getDate());
        booking.setTimeSlots(bookingRequest.getTimeSlots());
        booking.setUser(bookingRequest.getUser());
        booking.setVenue(bookingRequest.getVenue());
        booking.setStatus(Constants.BOOKING_STATUS_ACTIVE);

        bookingRepository.save(booking);
        emailUtils.sendEmailToUser(booking);

    }


    public List<BookingEventResponse> getAllBookingsBySlots() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingEventResponse> bookingEventResponses = new ArrayList<>();
        for (Booking booking : bookings) {
            for (TimeSlot timeSlot : booking.getTimeSlots()) {
                for (int i = 0; i < Integer.parseInt(timeSlot.getCourtBooked()); i++) {
                    BookingEventResponse bookingEventResponse = new BookingEventResponse();
                    bookingEventResponse.setId(booking.getId());
                    bookingEventResponse.setTitle(booking.getUser().getEmail());
                    bookingEventResponse.setStart(DateUtils.formattedTime(timeSlot.getTime()));
                    bookingEventResponse.setEnd(DateUtils.formattedTime(timeSlot.getTime()).plusHours(1));
                    bookingEventResponses.add(bookingEventResponse);
                }

                // bookingEventResponse.setSlotsBooked(Integer.parseInt(timeSlot.getCourtBooked()));
                //    bookingEventResponse.setSlotsEmpty(bookingEventResponse.getSlotsEmpty()-bookingEventResponse.getSlotsBooked());


            }
        }
        return bookingEventResponses;
    }


    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void cancelBooking(String bookingId) {

        Venue venue = venueService.getVenueDetails();
        Optional<Booking> booking = bookingRepository.findById(bookingId);

        LocalDate localDate = LocalDate.parse(booking.get().getDate());


        if (venue != null) {
            Optional<Availability.DailyAvailability> data = venue.getAvailability().getDailyAvailability().stream()
                    .filter(e -> e.getDate().isEqual(localDate))
                    .findFirst();

            data.ifPresent(dailyAvailability -> {
                for (TimeSlot timeSlot : booking.get().getTimeSlots()) {
                    Optional<Availability.DailyAvailability.HourlyAvailability> hourlyAvailability = dailyAvailability.getHourlyAvailability().stream()
                            .filter(item -> {
                                try {
                                    return item.getTime().equals(DateUtils.dateFormatter(timeSlot.getTime()));
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .findFirst();

                    hourlyAvailability.ifPresent(availability -> {
                        availability.setCourtAvailable(availability.getCourtAvailable() + Integer.parseInt(timeSlot.getCourtBooked()));
                    });
                }
            });

            venueService.save(venue);

            booking.get().setStatus(Constants.BOOKING_STATUS_CANCELED_BY_ADMIN);

            bookingRepository.save(booking.get());

            emailUtils.sendEmailToUser(booking.get());


        }
    }

}
