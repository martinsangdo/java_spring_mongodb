package com.t3h.java.module3.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.t3h.java.module3.model.Author;
import com.t3h.java.module3.model.Song;
import com.t3h.java.module3.model.SongWithAuthor;
import com.t3h.java.module3.repository.AuthorRepository;
import com.t3h.java.module3.repository.SongRepository;

@Service
public class SongService {
    @Autowired
    SongRepository songRepository;
    @Autowired
    AuthorRepository authorRepository;
    
    public Song getSongDetail(Long id){
        Song song = songRepository.findBy_id(id);
        return song;
    }
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

    public long updateDurationsToFormatted() {
        List<Song> songs = songRepository.findAll();
        long updatedCount = 0;

        for (Song song : songs) {
            if (song.getDuration() != null) {
                long totalSeconds = song.getDuration();
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;

                String formatted = String.format("%d:%02d", minutes, seconds);

                // update new field
                song.setFormattedDuration(formatted);
                // song.setDuration(null); // optional

                updatedCount++;
            }
        }

        if (updatedCount > 0) {
            songRepository.saveAll(songs);
        }

        return updatedCount;
    }

    public boolean deleteSongById(Long id) {
        if (songRepository.existsById(id)) {
            songRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public void updateSong(Long id, Song updatedSong){
        Song existing = songRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid song id: " + id));

        existing.setTitle(updatedSong.getTitle());
        existing.setYear(updatedSong.getYear());
        existing.setDuration(updatedSong.getDuration());
        existing.setAuthorId(updatedSong.getAuthorId());

        songRepository.save(existing);
    }
    //authors
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    public Page<Author> findAllAuthorsPagination(Pageable pageable){
        return authorRepository.findAll(pageable);
    }
    public List<Author> getAuthorsByCountry(String country) {
        return authorRepository.findByCountry(country);
    }

    public List<String> getAllCountries() {
        return authorRepository.findAll()
                .stream()
                .map(Author::getCountry)
                .distinct()
                .toList();
    }

    public List<SongWithAuthor> getAllSongsWithAuthors() {
        List<Song> songs = songRepository.findAll();
        Map<Long, String> authorMap = authorRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Author::get_id, Author::getName));

        return songs.stream()
                .map(s -> new SongWithAuthor(
                        s.get_id(),
                        s.getTitle(),
                        s.getYear(),
                        authorMap.getOrDefault(s.getAuthorId(), "Unknown"),
                        s.getFormattedDuration()
                ))
                .toList();
    }
}
