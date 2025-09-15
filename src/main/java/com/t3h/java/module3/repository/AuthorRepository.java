package com.t3h.java.module3.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.t3h.java.module3.model.Author;

@Repository
public interface AuthorRepository extends MongoRepository<Author, Long> {
    List<Author> findByCountry(String country);
}
