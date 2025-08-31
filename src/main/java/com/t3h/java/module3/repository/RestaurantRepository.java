package com.t3h.java.module3.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.t3h.java.module3.model.Restaurant;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
    List<Restaurant> findByBorough(String borough);
    Restaurant findFirstByRestaurantId(String id);
}
