package com.t3h.java.module3.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t3h.java.module3.model.Restaurant;
import com.t3h.java.module3.repository.RestaurantRepository;

@Service
public class RestaurantService {
    @Autowired
    RestaurantRepository restaurantRepository;
    
    public String handleUpload(MultipartFile file, Model uiModel){
        if (file == null || file.isEmpty()) {
            return "Please select a JSON file to upload.";
        }

        int count = 0;  //how many items are saved
        List<Restaurant> batch = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skip blank lines
                // Parse each line as a Restaurant
                Restaurant r = new ObjectMapper().readValue(line, Restaurant.class);
                batch.add(r);
                count++;
                // Insert in batches to avoid OOM for big files
                if (batch.size() >= 500) {
                    restaurantRepository.saveAll(batch);
                    batch.clear();
                }
            }
            // Save any remaining records
            if (!batch.isEmpty()) {
                restaurantRepository.saveAll(batch);
            }
        } catch (Exception e) {
            return "Failed to import file: " + e.getMessage();
        }

        return "File is uploaded successfully with " + count + " documents";
    }
}
