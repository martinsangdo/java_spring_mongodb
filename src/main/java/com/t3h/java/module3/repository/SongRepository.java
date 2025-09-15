package com.t3h.java.module3.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.t3h.java.module3.model.Song;

@Repository
public interface SongRepository extends MongoRepository<Song, Long> {
    Song findBy_id(Long id);
    List<Song> findTop5ByOrderByYearDesc();
    List<Song> findByAuthorId(Long authorId);

    @Query("SELECT s.author_id, COUNT(s) " +
           "FROM Songs s GROUP BY s.author_id ORDER BY COUNT(s) DESC")
    List<Object[]> countSongsByAuthor();
    boolean existsById(Long songId);

    @Query("{ 'duration': { $gte: ?0, $lte: ?1 }, 'year': { $gte: ?2, $lte: ?3 } }")
    List<Song> findByDurationRangeAndYear(Long minDuration, Long maxDuration, Integer minYear, Integer maxYear);
}
