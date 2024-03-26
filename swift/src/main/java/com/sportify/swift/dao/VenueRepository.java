package com.sportify.swift.dao;

import com.sportify.swift.entity.Venue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "venue", path = "venue")
public interface VenueRepository extends MongoRepository<Venue, String
        > {

    Venue findByBusinessName(String name);
}
