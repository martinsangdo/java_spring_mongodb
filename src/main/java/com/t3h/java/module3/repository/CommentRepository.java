package com.t3h.java.module3.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.t3h.java.module3.model.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String>  {

}
