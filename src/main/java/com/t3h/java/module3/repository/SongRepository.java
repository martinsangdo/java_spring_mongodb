package com.t3h.java.module3.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.t3h.java.module3.model.Song;

@Repository
public interface SongRepository extends MongoRepository<Song, String> {
        List<Song> findTop5ByOrderByYearDesc();

}
