package com.sportify.swift.service;

import com.sportify.swift.dao.BookingRepository;
import com.sportify.swift.entity.Availability;
import com.sportify.swift.entity.Booking;
import com.sportify.swift.entity.User;
import com.sportify.swift.entity.Venue;
import com.sportify.swift.requestmodel.BookingRequest;
import com.sportify.swift.requestmodel.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    VenueService venueService;

    @Autowired
    EmailService emailService;

    public void addNewBooking(BookingRequest bookingRequest) {

        LocalDate localDate = LocalDate.parse(bookingRequest.getDate());



        Venue venue = venueService.getAVenue(bookingRequest.getVenue().getId());

        if (venue != null) {
            Optional<Availability.DailyAvailability> data = venue.getAvailability().getDailyAvailability().stream()
                    .filter(e -> e.getDate().isEqual(localDate))
                    .findFirst();

            data.ifPresent(dailyAvailability -> {
                for (TimeSlot timeSlot : bookingRequest.getTimeSlots()) {
                    Optional<Availability.DailyAvailability.HourlyAvailability> hourlyAvailability = dailyAvailability.getHourlyAvailability().stream()
                            .filter(item -> {
                                try {
                                    return item.getTime().equals(dateFormatter(timeSlot.getTime()));
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




        bookingRepository.save(booking);

        sendEmailToUser(booking);

    }



    private void sendEmailToUser(Booking booking) {

        String userEmail = booking.getUser().getEmail();
        String subject = "Your Booking at "+booking.getVenue().getBusinessName();
        String text = generateConfirmationMessage(booking);


        emailService.sendEmail(userEmail,subject,text);

    }
    public String generateConfirmationMessage(Booking booking) {
        StringBuilder message = new StringBuilder();
        message.append("Booking Confirmation\n\n");
        message.append("Booking ID: ").append(booking.getId()).append("\n");
        message.append("Date: ").append(booking.getDate()).append("\n");
        message.append("Time Slots:\n");
        for (TimeSlot slot : booking.getTimeSlots()) {
            message.append("- ").append(slot.getTime()).append(": ").append(slot.getCourtBooked()).append(" courts\n");
        }
        message.append("\n");
        message.append("User Information:\n");
        message.append("- Name: ").append(booking.getUser().getName()).append("\n");
        message.append("- Email: ").append(booking.getUser().getEmail()).append("\n");
        message.append("- Phone: ").append(booking.getUser().getPhone()).append("\n\n");
        message.append("Venue Information:\n");
        message.append("- Business Name: ").append(booking.getVenue().getBusinessName()).append("\n");
        message.append("- Address: ").append(booking.getVenue().getAddress()).append(", ").append(booking.getVenue().getCity()).append("\n");
        return message.toString();
    }

    private Date  dateFormatter(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");



            Date date = dateFormat.parse(dateString);

//            System.out.println("Parsed Date: " + date);
//
//            // Get the Eastern Daylight Time (EDT) timezone
//            TimeZone edtTimeZone = TimeZone.getTimeZone("America/New_York");
//
//            // Set the time zone of the date object to EDT
//            dateFormat.setTimeZone(edtTimeZone);
//            String edtDate = dateFormat.format(date);
//
//            // Print the date in EDT
//            System.out.println("Date and Time in EDT: " + edtDate);


        return date;
    }
}
