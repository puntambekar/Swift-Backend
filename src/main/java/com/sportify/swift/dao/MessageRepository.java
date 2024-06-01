package com.sportify.swift.dao;

import com.sportify.swift.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "message", path = "mesasge")
public interface MessageRepository extends MongoRepository<Message,String> {
}
