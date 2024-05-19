package com.sportify.swift.dao;

import com.sportify.swift.entity.VenueList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "venuelist", path = "venuelist")
public interface VenueListRepository extends MongoRepository<VenueList,Integer> {
}
