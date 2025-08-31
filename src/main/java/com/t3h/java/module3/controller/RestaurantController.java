package com.t3h.java.module3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.t3h.java.module3.model.Restaurant;
import com.t3h.java.module3.service.RestaurantService;


@Controller
public class RestaurantController {
    @Autowired
    private SpringTemplateEngine templateEngine; // Inject Thymeleaf's template engine
    
    //show the UI page
    @GetMapping(value = "/restaurants/upload", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String showUploadPage() {
        Context context = new Context();
        return templateEngine.process("unit4", context);
    }

    @Autowired
    RestaurantService restaurantService;
    //handle upload file
    @PostMapping("/restaurants/upload")
    @ResponseBody
    public String handleUpload(@RequestParam("file") MultipartFile file, Model model) {
        String uploadResult = restaurantService.handleUpload(file, model);
        return uploadResult;
    }

    //pagination
    @GetMapping(value = "/restaurants", produces = MediaType.TEXT_HTML_VALUE)
    public String listRestaurants(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 10;
        Page<Restaurant> restaurantPage = restaurantService.findAllPagination(PageRequest.of(page, pageSize));
        int totalPages = restaurantPage.getTotalPages();
        int currentPage = page;
        // max number of pagination links to display
        int maxPagesToShow = 5;
        int startPage = Math.max(0, currentPage - maxPagesToShow / 2);
        int endPage = Math.min(totalPages - 1, startPage + maxPagesToShow - 1);
        // Adjust if we don’t have enough pages at the end
        if ((endPage - startPage) < (maxPagesToShow - 1)) {
            startPage = Math.max(0, endPage - (maxPagesToShow - 1));
        }
        model.addAttribute("list", restaurantPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "unit4_restaurants";
    }
}
