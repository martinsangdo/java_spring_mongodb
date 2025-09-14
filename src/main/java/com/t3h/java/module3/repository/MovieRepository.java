package com.t3h.java.module3.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.t3h.java.module3.model.Movie;

//we describe queries to a collection here
@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    List<Movie> findAll();

    Movie findByTitle(String title);
    // optional custom queries
    List<Movie> findByDirector(String director);
    List<Movie> findByYear(Integer year);
    List<Movie> findByTitleContainingIgnoreCase(String keyword);

    @Query("{ 'Vote_Average' : { $gte: ?0 } }")
    List<Movie> findHighlyRated(double minRating);
}
