package com.t3h.java.module3.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.t3h.java.module3.model.Movie;
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
        Page<Restaurant> restaurantPage = restaurantService.findAllPagination(PageRequest.of(page, pageSize, Sort.by("name").ascending().and(Sort.by("cuisine").ascending())));
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

    // Show detail form
    @GetMapping("/restaurants/detail/{id}")
    public String showRestaurantDetail(@PathVariable String id, Model model) {
        Restaurant restaurant = restaurantService.findById(id);
        model.addAttribute("restaurant", restaurant);
        return "unit4_restaurant_detail"; // Thymeleaf template
    }

    // Handle update
    @PostMapping("/restaurants/detail/{id}")
    public String updateRestaurant(@PathVariable String id,
                                   @ModelAttribute Restaurant updatedRestaurant, RedirectAttributes redirectAttributes) {
        Restaurant oldRestaurant = restaurantService.findById(id);
        restaurantService.updateDetail(oldRestaurant, updatedRestaurant);
        redirectAttributes.addFlashAttribute("message", "Update data successfully");
        return "redirect:/restaurants/detail/" + id;
    }

    @GetMapping("/api/restaurants/{id}/with-items")
    public ResponseEntity<List<HashMap>> getRestaurantWithItems(@PathVariable("id") String restaurantId) {
        List<HashMap> info = restaurantService.getRestaurantWithItems(restaurantId);
        return new ResponseEntity<List<HashMap>>(info, HttpStatus.OK);
    }
}
