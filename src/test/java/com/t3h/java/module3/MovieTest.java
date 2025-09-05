package com.t3h.java.module3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.t3h.java.module3.model.Movie;

class MovieTest {
    @Test
    void isHighlyRated_returnsTrue_whenRatingAboveThreshold() {
        Movie movie = new Movie();
        movie.setVote_Average(9.0);
        assertTrue(movie.isHighlyRated());
    }
    @Test
    void isHighlyRated_returnsFalse_whenRatingBelowThreshold() {
        Movie movie = new Movie();
        movie.setVote_Average(9.0);
        assertFalse(movie.isHighlyRated());
    }
}
