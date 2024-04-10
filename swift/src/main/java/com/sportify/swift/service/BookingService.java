package com.sportify.swift.service;

import com.sportify.swift.dao.BookingRepository;
import com.sportify.swift.entity.Booking;
import com.sportify.swift.entity.User;
import com.sportify.swift.requestmodel.BookingRequest;
import com.sportify.swift.requestmodel.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    EmailService emailService;

    public void addNewBooking(BookingRequest bookingRequest) {
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
}
