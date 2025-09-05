package com.t3h.java.module3;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import com.t3h.java.module3.model.Movie;
import com.t3h.java.module3.repository.MovieRepository;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
public class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;
    @Test
    void findHighlyRated_returnsMoviesAboveThreshold() {
        movieRepository.deleteAll();
        movieRepository.save(new Movie(6.0));
        movieRepository.save(new Movie(9.5));
        List<Movie> result = movieRepository.findHighlyRated(8.0);
        assertThat(result).hasSize(2);
    }
}
