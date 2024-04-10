package com.sportify.swift.dao;

import com.sportify.swift.entity.Availability;
import com.sportify.swift.entity.Venue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "venue", path = "venue")
public interface VenueRepository extends MongoRepository<Venue, String
        > {

    Venue findByBusinessName(String name);


}

