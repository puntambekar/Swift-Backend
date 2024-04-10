package com.sportify.swift.dao;

import com.sportify.swift.entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "booking", path = "booking")
public interface BookingRepository extends MongoRepository<Booking,String> {
}
