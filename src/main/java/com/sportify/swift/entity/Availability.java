package com.sportify.swift.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "availability")
@CompoundIndexes({
        @CompoundIndex(name = "year_month", def = "{'year': 1, 'month': 1}", unique = true)
})
public class Availability {
  //  private int venueId;
    @Id
    private String id;
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
            private LocalTime time;
            private int courtAvailable;
        }
    }


}
