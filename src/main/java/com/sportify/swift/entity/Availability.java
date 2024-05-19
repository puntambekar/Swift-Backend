package com.sportify.swift.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Availability {
  //  private int venueId;
    private int year;
    private int month;
    private List<DailyAvailability> dailyAvailability;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyAvailability {
        private LocalDate date;
        private List<HourlyAvailability> hourlyAvailability;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class HourlyAvailability {
            private Date time;
            private int courtAvailable;
        }
    }


}
