package com.t3h.java.module3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.t3h.java.module3.model.Comment;
import com.t3h.java.module3.model.Movie;
import com.t3h.java.module3.service.CommentService;
import com.t3h.java.module3.service.MovieService;

@Controller
public class MovieController {
    @Autowired
    MovieService movieService;
    @Autowired
    CommentService commentService;
    
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

    // @PostMapping("/api/movie/create")
    // public ResponseEntity<String> createMovie(@RequestBody Movie movie) {
    //     Movie newMovie = movieService.createNewMovie(movie);
    //     return new ResponseEntity<>(newMovie.getId(), HttpStatus.CREATED);
    // }

    // @PutMapping("/api/movie/update/{id}")
    // public ResponseEntity<String> updateMovie(@PathVariable String id, @RequestBody Movie movie) {
    //     Movie updated = movieService.updateMovie(id, movie);
    //     return new ResponseEntity<>(updated.getId(), HttpStatus.OK);
    // }

    @DeleteMapping("/api/movie/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable String id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>("Object is deleted", HttpStatus.OK);
    }

    //list movies with pagination
    @GetMapping(value = "/movies/home", produces = MediaType.TEXT_HTML_VALUE)
    public String showMovieList(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 9;
        Page<Movie> moviePage = movieService.findAllPagination(PageRequest.of(page, pageSize));
        int totalPages = moviePage.getTotalPages();
        int currentPage = page;
        // max number of pagination links to display
        int maxPagesToShow = 5;
        int startPage = Math.max(0, currentPage - maxPagesToShow / 2);
        int endPage = Math.min(totalPages - 1, startPage + maxPagesToShow - 1);
        // Adjust if we don’t have enough pages at the end
        if ((endPage - startPage) < (maxPagesToShow - 1)) {
            startPage = Math.max(0, endPage - (maxPagesToShow - 1));
        }
        System.out.println(moviePage.getContent());
        model.addAttribute("list", moviePage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "anime-main/categories";
    }

    //movie detail page
    @GetMapping(value = "/movies/detail", produces = MediaType.TEXT_HTML_VALUE)
    public String showMovieDetailPage(Model model) {
        return "anime-main/blog-details";
    }

    //create new comment
    @PostMapping("/movies/detail")
    public String createNewComment(Comment inputComment, RedirectAttributes redirectAttributes) {
        commentService.createNewComment(inputComment);
        redirectAttributes.addFlashAttribute("message", "Comment is saved");
        return "anime-main/blog-details";
    }

}
