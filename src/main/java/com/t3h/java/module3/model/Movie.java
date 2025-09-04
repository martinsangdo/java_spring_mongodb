package com.t3h.java.module3.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mymoviedb")    //collection name
public class Movie {
    private String Title;
    private String Overview;
    private String Release_Date;
    private Double Popularity;
    private Integer Vote_Count;
    private Double Vote_Average;
    private String Genre;
    private String Poster_Url;

    private String title;
    private Integer year;
    private List<String> genre;
    private String director;
    private Double rating;
}

// @Id
//     private String id;
