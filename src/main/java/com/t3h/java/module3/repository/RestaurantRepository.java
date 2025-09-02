package com.t3h.java.module3.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.t3h.java.module3.model.Restaurant;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
    List<Restaurant> findByBorough(String borough);
    Restaurant findFirstByRestaurantId(String id);

    @Query(value = "{ }", fields = "{ 'name': 1, 'address': 1 }")
    List<Restaurant> findAllNamesAndAddresses();

    @Query("{ $text: { $search: ?0 } }")
    List<Restaurant> searchByName(String keyword);

    
}
