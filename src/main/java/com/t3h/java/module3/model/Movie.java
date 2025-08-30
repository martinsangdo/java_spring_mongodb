package com.t3h.java.module3.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "movies")    //collection name
public class Movie {
    @Id
    private String id;
    private String title;
    private Integer year;
    private List<String> genre;
    private String director;
    private Double rating;
}
