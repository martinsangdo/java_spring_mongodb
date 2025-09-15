package com.t3h.java.module3.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.t3h.java.module3.model.Song;
import com.t3h.java.module3.service.SongService;

@Controller
public class SongController {
    @Autowired
    SongService songService;
    
    //3.1 1
    @GetMapping("/api/song/latest")
    public ResponseEntity<List<Song>> getLatestSongs() {
        return new ResponseEntity<>(songService.getLatestSongs(), HttpStatus.OK);
    }

    //3.1 2
    @GetMapping("/api/song/list_by_author")
    public ResponseEntity<List<Song>> findByAuthorId(@RequestParam(value = "author_id") Long authorId) {
        return new ResponseEntity<>(songService.findByAuthorId(authorId), HttpStatus.OK);
    }

    //3.1 3
    @GetMapping("/api/song/list_by_year")
    public ResponseEntity<Map<Long, Long>> countSongsByAuthor() {
        return new ResponseEntity<Map<Long, Long>>(songService.getSongCountByAuthor(), HttpStatus.OK);
    }

    @PutMapping("/api/song/update_duration")
    public ResponseEntity<Long> updateDurationsToFormatted() {
        return new ResponseEntity<Long>(songService.updateDurationsToFormatted(), HttpStatus.OK);
    }
}
