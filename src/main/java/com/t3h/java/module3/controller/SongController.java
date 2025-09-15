package com.t3h.java.module3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
