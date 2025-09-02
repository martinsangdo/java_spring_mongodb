package com.t3h.java.module3.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.t3h.java.module3.model.Item;

import java.util.List;

// public interface ItemRepository extends MongoRepository<Item, String> {
//     List<Item> findByRestaurantId(String restaurantId);
// }
public interface ItemRepository extends MongoRepository<Item, String>, ItemRepositoryCustom {

}
