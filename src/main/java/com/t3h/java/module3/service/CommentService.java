package com.t3h.java.module3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.t3h.java.module3.model.Comment;
import com.t3h.java.module3.repository.CommentRepository;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public Comment createNewComment(Comment params){
        return commentRepository.save(params);
    }

}