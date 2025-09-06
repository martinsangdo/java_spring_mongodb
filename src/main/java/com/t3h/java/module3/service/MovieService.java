package com.t3h.java.module3.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Map<String, Long> getAndGroupMoviesByLanguage() {
        List<Movie> movies = movieRepository.findAll();

        // Group by originalLanguage and count
        return movies.stream()
                     .collect(Collectors.groupingBy(
                         Movie::getOriginal_Language,
                         Collectors.counting()
                     ));
    }
    //mini project
    public Map<String, Object> getDataForDashboard(){
        Map<String, Object> results = new HashMap<>();
        //1. get total of movies grouped by month (7 months that have largest number of movies)
        List<Map<String, Object>> moviesByLatestMonth = getMovieCountByMonthBasic();
        results.put("moviesByLatestMonth", moviesByLatestMonth);
        System.out.println(moviesByLatestMonth);
        //


        //return all data
        return results;
    }
    public List<Map<String, Object>> getMovieCountByMonthBasic() {
        List<Movie> movies = movieRepository.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Count movies grouped by year-month
        Map<String, Long> countByMonth = movies.stream()
                .filter(m -> m.getRelease_Date() != null)
                .collect(Collectors.groupingBy(m -> {
                    LocalDate date = LocalDate.parse(m.getRelease_Date(), formatter);
                    return date.getYear() + "-" + String.format("%02d", date.getMonthValue());
                }, Collectors.counting()));

        // Sort by count (descending), then by month (descending for tie-breaking)
        List<Map.Entry<String, Long>> sortedEntries = new ArrayList<>(countByMonth.entrySet());
        sortedEntries.sort((a, b) -> {
            int cmp = b.getValue().compareTo(a.getValue());
            if (cmp == 0) {
                return b.getKey().compareTo(a.getKey()); // optional tie-breaker
            }
            return cmp;
        });

        // Take top 7
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < Math.min(7, sortedEntries.size()); i++) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("month", sortedEntries.get(i).getKey());
            obj.put("totalMovies", sortedEntries.get(i).getValue());
            result.add(obj);
        }

        return result;
    }
}
