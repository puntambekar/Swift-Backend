package com.sportify.swift.utils;

import com.sportify.swift.entity.Booking;
import com.sportify.swift.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmailUtils {

    @Autowired


    TemplateEngine templateEngine;

    @Autowired
    EmailService emailService;

    public String generateConfirmationMessageHTML(Booking booking) {
        Context context = new Context();
        context.setVariable("booking", booking);
        return templateEngine.process("confirmation.html", context);
    }
    public String generateCancelMessageHTML(Booking booking) {
        Context context = new Context();
        context.setVariable("booking", booking);
        return templateEngine.process("Cancellation.html", context);
    }
    public void sendEmailToUser(Booking booking) {
        String text = "";
        String userEmail = booking.getUser().getEmail();
        String subject = "Your Booking at " + booking.getVenue().getBusinessName();
        if(booking.getStatus().equals(Constants.BOOKING_STATUS_ACTIVE)){
             text = generateConfirmationMessageHTML(booking);
        }else
             text = generateCancelMessageHTML(booking);


        emailService.sendEmail(userEmail, subject, text);

    }


}
