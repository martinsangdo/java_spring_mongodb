package com.t3h.java.module3.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "restaurants")
public class Restaurant {
    @Id
    private String id;
    private String restaurant_id;
    private String name;
    private String borough;
    private String cuisine;
    private Address address;
    private List<Grade> grades;
}