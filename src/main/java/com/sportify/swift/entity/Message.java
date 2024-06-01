package com.sportify.swift.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection ="message" )
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private String id;

    private String name;
    private String email;
    private String message;
}
