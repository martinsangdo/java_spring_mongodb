package com.t3h.java.module3.service;

import java.util.List;

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
}
