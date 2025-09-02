package com.t3h.java.module3.repository;

import com.mongodb.client.result.UpdateResult;
import com.t3h.java.module3.model.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepositoryImpl implements ItemRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void updateRestaurantId(String oldId, String newId) {
        Query query = new Query(Criteria.where("restaurant_id").is(oldId));
        Update update = new Update().set("restaurant_id", newId);
        UpdateResult result = mongoTemplate.updateMulti(query, update, Item.class);
        System.out.println("Updated " + result.getModifiedCount() + " items with new restaurant_id.");
    }
}
