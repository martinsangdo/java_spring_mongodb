package com.t3h.java.module3.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document("songs")
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    @Id
    private Long _id;
    
    private String title;
    private Integer year;
    private Integer duration;
    @Field("author_id")
    private Long authorId;
}
