package com.t3h.java.module3.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t3h.java.module3.model.Item;
import com.t3h.java.module3.model.Restaurant;
import com.t3h.java.module3.repository.ItemRepository;
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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<Restaurant> batch = new ArrayList<>();
            count = buildBatch(reader, count, batch);
            if (!batch.isEmpty()) {
                restaurantRepository.saveAll(batch);
            }
        } catch (Exception e) {
            return "Failed to import file: " + e.getMessage();
        }
        return "File is uploaded successfully with " + count + " documents";
    }

    public int buildBatch(BufferedReader reader, int count, List<Restaurant> batch) throws IOException{
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue; // skip blank lines
            Restaurant r = new ObjectMapper().readValue(line, Restaurant.class);
            batch.add(r);
            count++;
            if (batch.size() >= 500) {
                restaurantRepository.saveAll(batch);
                batch.clear();
            }
        }
        return count;
    }

    public List<Restaurant> findAll(){
        return restaurantRepository.findAll();
    }

    public Page<Restaurant> findAllPagination(Pageable pageable){
        return restaurantRepository.findAll(pageable);
    }

    public Restaurant findById(String id){
        return restaurantRepository.findFirstByRestaurantId(id);
    }

    public Restaurant updateDetail(Restaurant oldObject, Restaurant newObject){
        if (Objects.nonNull(newObject.getName())){
            oldObject.setName(newObject.getName());
        }
        if (Objects.nonNull(newObject.getBorough())){
            oldObject.setBorough(newObject.getBorough());
        }
        if (Objects.nonNull(newObject.getCuisine())){
            oldObject.setCuisine(newObject.getCuisine());
        }
        return restaurantRepository.save(oldObject);
    }

    @Autowired
    private MongoTemplate mongoTemplate;
    public List<HashMap> getRestaurantWithItems(String restaurantId){
        MatchOperation matchStage = Aggregation.match(Criteria.where("restaurant_id").is(restaurantId));
        LookupOperation lookupStage = LookupOperation.newLookup()
                .from("items")                 // target collection
                .localField("restaurant_id")   // field in restaurants
                .foreignField("restaurant_id") // field in items
                .as("menuItems");              // alias field

        Aggregation aggregation = Aggregation.newAggregation(matchStage, lookupStage);
        return mongoTemplate.aggregate(aggregation, "restaurants", HashMap.class).getMappedResults();
    }

    public List<HashMap> getItemsWithRestaurantInfo(String restaurantId) {
        MatchOperation matchOperation = Aggregation.match(Criteria.where("restaurant_id").is(restaurantId));
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("restaurants")
                .localField("restaurant_id")
                .foreignField("restaurant_id")
                .as("restaurantInfo");
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                lookupOperation
        );
        AggregationResults<HashMap> results = mongoTemplate.aggregate(aggregation, "items", HashMap.class);
        return results.getMappedResults();
    }

    @Autowired
    private ItemRepository itemRepository;
    @Transactional
    public void updateRestaurantIdWithSave(String oldId, String newId) {
        // 1. Update in restaurants collection
        Restaurant restaurant = restaurantRepository.findFirstByRestaurantId(oldId);
        if (restaurant == null) {
            throw new RuntimeException("Restaurant not found with restaurant_id: " + oldId);
        }
        restaurant.setRestaurantId(newId);
        restaurantRepository.save(restaurant);
        // 2. Update in items collection (fetch → modify → save)
        //List<Item> items = itemRepository.findByRestaurantId(oldId);
        itemRepository.updateRestaurantId(oldId, newId);
        // for (Item item : items) {
        //     item.setRestaurantId(newId);
        //     itemRepository.save(item); // each save() updates one doc
        // }
        // Instead of page 1000 with skip(), use last seen ID:
        // Query query = new Query(Criteria.where("restaurant_id").gt(lastSeenId)).limit(50);

    }

    public void bulkUpdateRestaurants() {
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, "restaurants");
        // Example 1: Update borough for a specific restaurant
        Query query1 = new Query(Criteria.where("name").is("Wendy'S"));
        Update update1 = new Update().set("borough", "Brooklyn Center");
        bulkOps.updateOne(query1, update1);
        // Example 2: Update cuisine for multiple restaurants
        Query query2 = new Query(Criteria.where("cuisine").is("Bakery"));
        Update update2 = new Update().set("cuisine", "Pastry Shop");
        bulkOps.updateMulti(query2, update2);
        // Example 3: Add a new field to all restaurants
        Query query3 = new Query(); // matches all
        Update update3 = new Update().set("verified", true);
        bulkOps.updateMulti(query3, update3);
        // Execute all operations at once
        bulkOps.execute();
    }
}
