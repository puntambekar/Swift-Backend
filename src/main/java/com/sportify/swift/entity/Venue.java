package com.sportify.swift.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection ="venue" )
@AllArgsConstructor
@NoArgsConstructor
public class Venue {


        @Id
        private String id; // Use the default ObjectId field

        @Indexed(unique = true)
        private String businessName;
        private String address;
        private String city;




}
