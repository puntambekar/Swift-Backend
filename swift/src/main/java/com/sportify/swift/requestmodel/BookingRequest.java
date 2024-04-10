package com.sportify.swift.requestmodel;

import com.sportify.swift.entity.User;
import com.sportify.swift.entity.Venue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private String date;
    private List<TimeSlot> timeSlots;
    private User user;
    private Venue venue;


}
