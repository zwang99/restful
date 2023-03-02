package com.example.shop_project_detail.controller;

import com.example.shop_project_detail.domain.entity.Product;
import com.example.shop_project_detail.domain.entity.User;
import com.example.shop_project_detail.service.LoginService;
import com.example.shop_project_detail.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("user")
public class WatchListController {
    public WatchListService watchListService;
    public LoginService loginService;

    @Autowired
    public WatchListController(WatchListService watchListService, LoginService loginService) {
        this.watchListService = watchListService;
        this.loginService = loginService;
    }

    @PostMapping("/addWatchlist")
    public ResponseEntity<String> addToWatchList(@RequestParam("user_id") int user_id, @RequestParam("product_id") int product_id){
        String current_username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = loginService.getUserByUserId(user_id);
        if(user == null || !user.getUsername().equals(current_username)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }
        watchListService.addToWatchList(user_id, product_id);
        return ResponseEntity.ok().body("Add to Watchlist successfully!");
    }

    @GetMapping("/view_watchlist")
    public ResponseEntity<List<Product>> viewWatchList(@RequestParam("user_id") int user_id){
        String current_username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = loginService.getUserByUserId(user_id);
        if(user == null || !user.getUsername().equals(current_username)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
        }
        return ResponseEntity.ok().body(watchListService.getWatchlistByUserId(user_id));
    }

    @DeleteMapping("/delete_watchlist")
    public ResponseEntity<String> deleteFromWatchlist(@RequestParam("user_id") int user_id, @RequestParam("product_id") int product_id){
        String current_username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = loginService.getUserByUserId(user_id);
        if(user == null || !user.getUsername().equals(current_username)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }
        watchListService.removeFromWatchlist(user_id, product_id);
        return ResponseEntity.ok().body("Delete Successfully");
    }
}
