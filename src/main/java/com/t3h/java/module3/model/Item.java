package com.t3h.java.module3.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "items")
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String restaurantId;
    private String name;
    private String description;
    private Double price;
    private String category;
}
