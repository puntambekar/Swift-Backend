package com.sportify.swift.service;

import com.sportify.swift.dao.BookingRepository;
import com.sportify.swift.entity.Availability;
import com.sportify.swift.entity.Booking;
import com.sportify.swift.entity.Venue;
import com.sportify.swift.requestmodel.BookingRequest;
import com.sportify.swift.requestmodel.TimeSlot;
import com.sportify.swift.responsemodel.BookingEventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        String subject = "Your Booking at " + booking.getVenue().getBusinessName();
        String text = generateConfirmationMessageHTML(booking);


        emailService.sendEmail(userEmail, subject, text);

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

    public String generateConfirmationMessageHTML(Booking booking) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><head><style>");
        htmlContent.append("body { font-family: Arial, sans-serif; margin: 0; padding: 0;}");
        htmlContent.append(".header { background-color: #007bff; color: #fff; padding: 20px; text-align: center;}");
        htmlContent.append(".footer { background-color: #f8f9fa; color: #333; padding: 20px; text-align: center;}");
        htmlContent.append("</style></head><body>");
        htmlContent.append("<div class=\"header\"><h1>Booking Confirmation</h1></div>");
        htmlContent.append("<div><img src=\"https://example.com/your-logo.png\" alt=\"Logo\" style=\"display: block; margin: 0 auto; max-width: 100%; height: auto;\"></div>");
        htmlContent.append("<div>");
        htmlContent.append("<p><strong>Booking ID:</strong> ").append(booking.getId()).append("</p>");
        htmlContent.append("<p><strong>Date:</strong> ").append(booking.getDate()).append("</p>");
        htmlContent.append("<h3>Time Slots:</h3>");
        htmlContent.append("<ul>");
        for (TimeSlot slot : booking.getTimeSlots()) {
            htmlContent.append("<li>").append(slot.getTime()).append(": ").append(slot.getCourtBooked()).append(" courts</li>");
        }
        htmlContent.append("</ul>");
        htmlContent.append("<h3>User Information:</h3>");
        htmlContent.append("<ul>");
        htmlContent.append("<li><strong>Name:</strong> ").append(booking.getUser().getName()).append("</li>");
        htmlContent.append("<li><strong>Email:</strong> ").append(booking.getUser().getEmail()).append("</li>");
        htmlContent.append("<li><strong>Phone:</strong> ").append(booking.getUser().getPhone()).append("</li>");
        htmlContent.append("</ul>");
        htmlContent.append("<h3>Venue Information:</h3>");
        htmlContent.append("<ul>");
        htmlContent.append("<li><strong>Business Name:</strong> ").append(booking.getVenue().getBusinessName()).append("</li>");
        htmlContent.append("<li><strong>Address:</strong> ").append(booking.getVenue().getAddress()).append(", ").append(booking.getVenue().getCity()).append("</li>");
        htmlContent.append("</ul>");
        htmlContent.append("</div>");
        htmlContent.append("<div class=\"footer\"><p>Thank you for choosing our service!</p></div>");
        htmlContent.append("</body></html>");
        return htmlContent.toString();
    }


    private Date dateFormatter(String dateString) throws ParseException {
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

    public List<BookingEventResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingEventResponse> bookingEventResponses = new ArrayList<>();
        for (Booking booking : bookings) {
            for (TimeSlot timeSlot : booking.getTimeSlots()) {
              for(int i=0;i<Integer.parseInt(timeSlot.getCourtBooked());i++){
                  BookingEventResponse bookingEventResponse = new BookingEventResponse();
                  bookingEventResponse.setId(booking.getId());
                  bookingEventResponse.setTitle(booking.getUser().getEmail());
                  bookingEventResponse.setStart(formattedTime(timeSlot.getTime()));
                  bookingEventResponse.setEnd(formattedTime(timeSlot.getTime()).plusHours(1));
                  bookingEventResponses.add(bookingEventResponse);
              }

               // bookingEventResponse.setSlotsBooked(Integer.parseInt(timeSlot.getCourtBooked()));
            //    bookingEventResponse.setSlotsEmpty(bookingEventResponse.getSlotsEmpty()-bookingEventResponse.getSlotsBooked());


            }
        }
        return bookingEventResponses;
    }

    private ZonedDateTime formattedTime(String time) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(time, formatter);
        return zonedDateTime;
    }
}
