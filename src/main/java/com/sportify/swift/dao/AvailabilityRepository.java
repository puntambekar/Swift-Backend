package com.sportify.swift.dao;


import com.sportify.swift.entity.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "availability", path = "availability")
public interface AvailabilityRepository extends MongoRepository<Availability,String> {
    Availability findByYearAndMonth(int year, int month);
}
