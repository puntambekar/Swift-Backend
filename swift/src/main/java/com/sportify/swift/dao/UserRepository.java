package com.sportify.swift.dao;

import com.sportify.swift.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "user",path = "user")
public interface UserRepository extends MongoRepository<User,String> {
}
