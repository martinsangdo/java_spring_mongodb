package com.t3h.java.module3.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Document(collection = "restaurants")
public class Restaurant {
    @Id
    private String id;
    @Field("restaurant_id")
    @JsonProperty("restaurant_id")
    private String restaurantId;
    private String name;
    private String borough;
    private String cuisine;
    private Address address;
    private List<Grade> grades;
}