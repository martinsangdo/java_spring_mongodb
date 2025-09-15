package com.t3h.java.module3.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.t3h.java.module3.model.Author;
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
    //3.2
    @PutMapping("/api/song/update_duration")
    public ResponseEntity<Long> updateDurationsToFormatted() {
        return new ResponseEntity<Long>(songService.updateDurationsToFormatted(), HttpStatus.OK);
    }

    //3.3
    @DeleteMapping("/api/song/{id}")
    public ResponseEntity<Map<String, Object>> deleteSong(@PathVariable Long id) {
        boolean deleted = songService.deleteSongById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", deleted);

        if (deleted) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }
    //authors
    //pagination
    @GetMapping(value = "/authors", produces = MediaType.TEXT_HTML_VALUE)
    public String listAuthors(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 3;
        Page<Author> authorPage = songService.findAllAuthorsPagination(PageRequest.of(page, pageSize, Sort.by("birth_year").descending()));
        int totalPages = authorPage.getTotalPages();
        int currentPage = page;
        // max number of pagination links to display
        int maxPagesToShow = 3;
        int startPage = Math.max(0, currentPage - maxPagesToShow / 2);
        int endPage = Math.min(totalPages - 1, startPage + maxPagesToShow - 1);
        // Adjust if we don’t have enough pages at the end
        if ((endPage - startPage) < (maxPagesToShow - 1)) {
            startPage = Math.max(0, endPage - (maxPagesToShow - 1));
        }
        model.addAttribute("list", authorPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "author_pagination";
    }

}
