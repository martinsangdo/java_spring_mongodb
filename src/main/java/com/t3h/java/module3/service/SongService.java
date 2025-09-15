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
}
