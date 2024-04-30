package com.sportify.swift.responsemodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingEventResponse {
    String id;
    String title;
    ZonedDateTime start;
    ZonedDateTime end;
   // int slotsBooked;
   // int slotsEmpty=10;
}
