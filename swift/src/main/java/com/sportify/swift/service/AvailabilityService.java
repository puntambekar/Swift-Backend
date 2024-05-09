package com.sportify.swift.service;

import com.sportify.swift.dao.AvailabilityRepository;
import com.sportify.swift.entity.Availability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AvailabilityService {

    @Autowired
    AvailabilityRepository availabilityRepository;

    @Value("${weekend.startTime}")
    int weekend_startTime;
    @Value("${weekend.endTime}")
    int weekend_endTime;
    @Value("${weekday.startTime}")
    int weekday_startTime;
    @Value("${weekday.endTime}")
    int weekday_endTime;


    public Availability createDummyAvailabilityData(int year,int month) {

        Availability availability = new Availability();
        availability.setYear(year);
        availability.setMonth(month);

        YearMonth yearMonth = YearMonth.of(year,month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate= yearMonth.atEndOfMonth();

        List<Availability.DailyAvailability> dailyAvailabilities = new ArrayList<>();


        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            Availability.DailyAvailability dailyAvailability = new Availability.DailyAvailability();
            dailyAvailability.setDate(date);

            List<Availability.DailyAvailability.HourlyAvailability> hourlyAvailabilities = new ArrayList<>();
            if(date.getDayOfWeek()==DayOfWeek.SATURDAY||date.getDayOfWeek()==DayOfWeek.SUNDAY){
                for (int hour = weekend_startTime; hour <= weekend_endTime; hour++) {
                    hourlyAvailabilities.add(
                            new Availability.DailyAvailability.HourlyAvailability(
                                    Date.from(LocalTime.of(hour, 0).atDate(date).atZone(ZoneId.of("America/New_York")).toInstant()), 10));
                }
            }else {
                for (int hour = weekday_startTime; hour <= weekday_endTime; hour++) {
                    hourlyAvailabilities.add(
                            new Availability.DailyAvailability.HourlyAvailability(
                                    Date.from(LocalTime.of(hour, 0).atDate(date).atZone(ZoneId.of("America/New_York")).toInstant()), 10));
                }
            }

            dailyAvailability.setHourlyAvailability(hourlyAvailabilities);
            dailyAvailabilities.add(dailyAvailability);
        }

        availability.setDailyAvailability(dailyAvailabilities);
        System.out.println(availability);

        return saveAvailability(availability);

    }

    public Availability saveAvailability(Availability newAvailability) {

        Availability existingAvailability = availabilityRepository.findByYearAndMonth(newAvailability.getYear(), newAvailability.getMonth());

        if (existingAvailability != null) {

            existingAvailability.setDailyAvailability(newAvailability.getDailyAvailability());
            return availabilityRepository.save(existingAvailability); // Update the existing document
        } else {

            return availabilityRepository.save(newAvailability);
        }
    }

    public List<Availability.DailyAvailability.HourlyAvailability> getAvailabilityForDay(String dateString){

        LocalDate localDate = LocalDate.parse(dateString);
        int year = localDate.getYear();
        int month = localDate.getMonthValue();

        Availability availability = availabilityRepository.findByYearAndMonth(year,month);


        Optional<Availability.DailyAvailability> data = availability.getDailyAvailability().stream().filter(e -> e.getDate() .isEqual(localDate) ).findFirst();


        return data.get().getHourlyAvailability();
    }

    public List<Availability> getAvailabilityDataForThreeMonths() {

        LocalDate localDate = LocalDate.now();
        int currentYear = localDate.getYear();
       int currentMonth= localDate.getMonthValue();

       List<Availability> availabilities = new ArrayList<>();
       availabilities.add(availabilityRepository.findByYearAndMonth(currentYear,currentMonth));
       availabilities.add(availabilityRepository.findByYearAndMonth(currentYear,currentMonth+1));
       availabilities.add(availabilityRepository.findByYearAndMonth(currentYear,currentMonth+2));
       return availabilities;

    }

    public Availability getAvailabilityDataByMonth(String yearString,String monthString) {
        int year = Integer.parseInt(yearString);
        int month = Integer.parseInt(monthString);
       Availability monthlyAvailability = availabilityRepository.findByYearAndMonth(year,month);
       if(monthlyAvailability==null){
           monthlyAvailability=createDummyAvailabilityData(year,month);
       }

        return monthlyAvailability;
    }

    public void updateAvailabilityData(Availability availability) {
        availabilityRepository.save(availability);
    }
}
