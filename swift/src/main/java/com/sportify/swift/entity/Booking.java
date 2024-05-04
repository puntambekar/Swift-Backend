package com.sportify.swift.entity;

import com.sportify.swift.requestmodel.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection ="booking" )
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    private String id;
    private String date;
    private List<TimeSlot> timeSlots;
    private User user;
    private Venue venue;
    private String status;
}
