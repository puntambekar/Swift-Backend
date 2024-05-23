package com.sportify.swift.responsemodel;

import com.sportify.swift.entity.Availability;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueResponse {

    private String id; // Use the default ObjectId field
    private String businessName;
    private String address;
    private String city;
    private List<Availability> availability;

}
