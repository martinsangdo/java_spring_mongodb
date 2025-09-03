package com.t3h.java.module3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.t3h.java.module3.model.Movie;
import com.t3h.java.module3.model.Restaurant;
import com.t3h.java.module3.service.MovieService;

@Controller
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

    @PostMapping("/api/movie/create")
    public ResponseEntity<String> createMovie(@RequestBody Movie movie) {
        Movie newMovie = movieService.createNewMovie(movie);
        return new ResponseEntity<>(newMovie.getId(), HttpStatus.CREATED);
    }

    @PutMapping("/api/movie/update/{id}")
    public ResponseEntity<String> updateMovie(@PathVariable String id, @RequestBody Movie movie) {
        Movie updated = movieService.updateMovie(id, movie);
        return new ResponseEntity<>(updated.getId(), HttpStatus.OK);
    }

    @DeleteMapping("/api/movie/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable String id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>("Object is deleted", HttpStatus.OK);
    }

    @GetMapping("/movies/home")
    public String showMoviesHomepage(Model model) {
        return "anime-main/index";
    }
}
