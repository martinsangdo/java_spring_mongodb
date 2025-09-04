package com.t3h.java.module3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.t3h.java.module3.model.Comment;
import com.t3h.java.module3.model.Restaurant;
import com.t3h.java.module3.service.CommentService;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;

    

}