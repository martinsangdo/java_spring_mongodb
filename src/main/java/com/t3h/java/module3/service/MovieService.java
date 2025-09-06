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
        //2) get total movies by each genre
        List<Map<String, Object>> moviesByGenre = getMovieCountByGenre();
        results.put("moviesByGenre", moviesByGenre);
        //3) get 4 top languages
        List<Map<String, Object>> top4Languages = getTop4Languages();
        results.put("top4Languages", top4Languages);
        //4)
        List<Movie> top5ByVoteAverage = getTop5MoviesByVoteAverage();
        results.put("top5ByVoteAverage", top5ByVoteAverage);
        //5)
        List<Movie> top5ByReleaseDate = get5LatestMovies();
        results.put("top5ByReleaseDate", top5ByReleaseDate);
        
        //return all data
        return results;
    }
    //
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
    //
    public List<Map<String, Object>> getMovieCountByGenre() {
        List<Movie> movies = movieRepository.findAll();
    
        Map<String, Long> genreCount = new HashMap<>();
    
        for (Movie movie : movies) {
            if (movie.getGenre() != null && !movie.getGenre().isEmpty()) {
                String[] genres = movie.getGenre().split(",");
                for (String g : genres) {
                    String genre = g.trim();
                    genreCount.put(genre, genreCount.getOrDefault(genre, 0L) + 1);
                }
            }
        }
    
        // Convert to list of maps for JSON response
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : genreCount.entrySet()) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("genre", entry.getKey());
            obj.put("totalMovies", entry.getValue());
            result.add(obj);
        }
    
        // Sort by totalMovies descending
        result.sort((a, b) -> ((Long) b.get("totalMovies")).compareTo((Long) a.get("totalMovies")));
    
        return result;
    }
    //
    public List<Map<String, Object>> getTop4Languages() {
        List<Movie> movies = movieRepository.findAll();
    
        // Count movies grouped by language
        Map<String, Long> langCount = movies.stream()
                .filter(m -> m.getOriginal_Language() != null && !m.getOriginal_Language().isEmpty())
                .collect(Collectors.groupingBy(Movie::getOriginal_Language, Collectors.counting()));
    
        // Sort by count descending
        List<Map.Entry<String, Long>> sorted = new ArrayList<>(langCount.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
    
        // Take top 4
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < Math.min(4, sorted.size()); i++) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("language", sorted.get(i).getKey());
            obj.put("totalMovies", sorted.get(i).getValue());
            result.add(obj);
        }
    
        return result;
    }
    //4
    public List<Movie> getTop5MoviesByVoteAverage() {
        List<Movie> movies = movieRepository.findAll();
    
        return movies.stream()
                .filter(m -> m.getTitle() != null && !m.getTitle().trim().isEmpty()) // exclude empty titles
                .filter(m -> m.getVote_Average() != null && m.getVote_Count() != null) // avoid nulls
                .sorted((a, b) -> {
                    int cmp = Double.compare(b.getVote_Average(), a.getVote_Average());
                    if (cmp == 0) {
                        return Integer.compare(b.getVote_Count(), a.getVote_Count());
                    }
                    return cmp;
                })
                .limit(5)
                .collect(Collectors.toList());
    }
    //5
    public List<Movie> get5LatestMovies() {
        List<Movie> movies = movieRepository.findAll();
    
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
        return movies.stream()
                .filter(m -> m.getTitle() != null && !m.getTitle().trim().isEmpty()) // exclude empty title
                .filter(m -> m.getRelease_Date() != null && !m.getRelease_Date().isEmpty()) // valid date
                .filter(m -> m.getVote_Average() != null && m.getVote_Count() != null) // avoid nulls
                .sorted((a, b) -> {
                    // Parse release dates
                    LocalDate dateA = LocalDate.parse(a.getRelease_Date(), formatter);
                    LocalDate dateB = LocalDate.parse(b.getRelease_Date(), formatter);
    
                    // First: latest release date
                    int cmp = dateB.compareTo(dateA);
                    if (cmp == 0) {
                        // Second: vote average
                        cmp = Double.compare(b.getVote_Average(), a.getVote_Average());
                    }
                    if (cmp == 0) {
                        // Third: vote count
                        cmp = Integer.compare(b.getVote_Count(), a.getVote_Count());
                    }
                    return cmp;
                })
                .limit(5)
                .collect(Collectors.toList());
    }
    
    
    
    
}
