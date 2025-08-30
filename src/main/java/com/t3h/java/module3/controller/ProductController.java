package com.t3h.java.module3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    
    @GetMapping("/")
    public ResponseEntity<String> homepage(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }
}
