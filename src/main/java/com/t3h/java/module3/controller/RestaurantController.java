package com.t3h.java.module3.controller;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t3h.java.module3.model.Movie;
import com.t3h.java.module3.model.Restaurant;
import com.t3h.java.module3.service.RestaurantService;

@RestController
public class RestaurantController {
    @Autowired
    private SpringTemplateEngine templateEngine; // Inject Thymeleaf's template engine
    
    //show the UI page
    @GetMapping(value = "/restaurants/upload", produces = MediaType.TEXT_HTML_VALUE)
    public String showUploadPage() {
        Context context = new Context();
        return templateEngine.process("unit4", context);
    }

    @Autowired
    RestaurantService restaurantService;
    //handle upload file
    @PostMapping("/restaurants/upload")
    public String handleUpload(@RequestParam("file") MultipartFile file, Model model) {
        String uploadResult = restaurantService.handleUpload(file, model);
        return uploadResult;
    }

    @GetMapping("/api/restaurants")
    public ResponseEntity<List<Restaurant>> findAllRestaurants(){
        List<Restaurant> list = restaurantService.findAll();
        return new ResponseEntity<List<Restaurant>>(list, HttpStatus.OK);
    }

    @GetMapping(value="/restaurants", produces = MediaType.TEXT_HTML_VALUE)
    public String showPageList(){
        List<Restaurant> list = restaurantService.findAll();

        Context context = new Context();
        context.setVariable("list", list);
        return templateEngine.process("unit4_restaurants", context);
    }
}
