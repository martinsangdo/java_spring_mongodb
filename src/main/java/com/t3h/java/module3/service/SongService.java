package com.t3h.java.module3.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.t3h.java.module3.model.Song;
import com.t3h.java.module3.repository.SongRepository;

@Service
public class SongService {
    @Autowired
    SongRepository songRepository;
    
    public List<Song> getLatestSongs() {
        return songRepository.findTop5ByOrderByYearDesc();
    }

    public List<Song> findByAuthorId(Long authorId) {
        return songRepository.findByAuthorId(authorId);
    }

    public Map<Long, Long> getSongCountByAuthor() {
        List<Song> songs = songRepository.findAll();

        return songs.stream()
                .collect(Collectors.groupingBy(Song::getAuthorId, Collectors.counting()));
    }
}
