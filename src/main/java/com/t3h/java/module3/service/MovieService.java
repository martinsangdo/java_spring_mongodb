package com.t3h.java.module3.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.t3h.java.module3.model.Movie;
import com.t3h.java.module3.model.Restaurant;
import com.t3h.java.module3.repository.MovieRepository;

@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;
    
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public List<Movie> searchByKeyword(String keyString){
        System.out.println(keyString);
        return movieRepository.findByTitleContainingIgnoreCase(keyString);
    }

    public Movie createNewMovie(Movie params){
        return movieRepository.save(params);
    }

    public Movie updateMovie(String id, Movie params){
        Movie existing = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        // Update fields
        if (Objects.nonNull(params.getTitle()))
            existing.setTitle(params.getTitle());
        // if (Objects.nonNull(params.getYear()))
        //     existing.setYear(params.getYear());
        // if (Objects.nonNull(params.getGenre()))
        //     existing.setGenre(params.getGenre());
        // if (Objects.nonNull(params.getDirector()))
        //     existing.setDirector(params.getDirector());
        // if (Objects.nonNull(params.getRating()))
        //     existing.setRating(params.getRating());

        return movieRepository.save(existing);
    }

    public void deleteMovie(String id){
        Movie existing = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        movieRepository.delete(existing);
    }

    public Page<Movie> findAllPagination(Pageable pageable){
        return movieRepository.findAll(pageable);
    }
}
