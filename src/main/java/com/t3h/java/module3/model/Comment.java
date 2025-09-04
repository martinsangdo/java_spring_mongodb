package com.t3h.java.module3.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "comments")
public class Comment {
    private String name;
    private String email;
    private String message;
}
