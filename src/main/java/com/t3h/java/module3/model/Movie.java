package com.t3h.java.module3.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "mymoviedb")    //collection name
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Field("Title")
    private String title;
    @Field("Overview")
    private String overview;
    @Field("Release_Date")
    private String releaseDate;
    @Field("Popularity")
    private Double popularity;
    @Field("Vote_Count")
    private Integer voteCount;
    @Field("Vote_Average")
    private Double voteAverage;
    @Field("Genre")
    private String genre;
    @Field("Poster_Url")
    private String posterUrl;
    @Field("Original_Language")
    private String originalLanguage;
    private Integer year;
    private String director;
    private Double rating;

    public Movie(Double vote){
        this.voteAverage = vote;
    }

    public boolean isHighlyRated() {
        return this.voteAverage >= 8.0;
    }

}

// @Id
//     private String id;
