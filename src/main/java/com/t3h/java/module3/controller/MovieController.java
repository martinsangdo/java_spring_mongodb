package com.t3h.java.module3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.t3h.java.module3.model.Movie;
import com.t3h.java.module3.service.MovieService;

@RestController
public class MovieController {
    @Autowired
    MovieService movieService;
    
    @GetMapping("/")
    public ResponseEntity<String> homepage(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> findAllMovies(){
        List<Movie> movies = movieService.getAllMovies();
        return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);
    }

    @GetMapping("/movie/search")
    public ResponseEntity<List<Movie>> searchByKeyword(@RequestParam String keyword){
        List<Movie> movies = movieService.searchByKeyword(keyword);
        return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);
    }
}
