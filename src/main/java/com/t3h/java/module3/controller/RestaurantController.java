package com.t3h.java.module3.controller;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import com.t3h.java.module3.model.Restaurant;
import com.t3h.java.module3.service.RestaurantService;

@RestController
public class RestaurantController {
    @Autowired
    private SpringTemplateEngine templateEngine; // Inject Thymeleaf's template engine
    
    @GetMapping(value = "/restaurants/upload", produces = MediaType.TEXT_HTML_VALUE)
    public String importDataFromFile() {
        Context context = new Context();
        return templateEngine.process("unit4", context);
    }

    @Autowired
    RestaurantService restaurantService;

    @PostMapping("/restaurants/upload")
    public String handleUpload(@RequestParam("file") MultipartFile file, Model model) {
        String uploadResult = restaurantService.handleUpload(file, model);
        return uploadResult;
    }
}
