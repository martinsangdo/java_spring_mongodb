package com.t3h.java.module3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.t3h.java.module3.model.Movie;
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

}
