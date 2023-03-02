package com.example.shop_project_detail.controller;


import com.example.shop_project_detail.domain.entity.Order;
import com.example.shop_project_detail.domain.entity.Product;
import com.example.shop_project_detail.domain.entity.User;
import com.example.shop_project_detail.domain.request.OrderRequest;
import com.example.shop_project_detail.domain.response.UserOrderResponse;
import com.example.shop_project_detail.domain.response.MostPurchasedResponse;
import com.example.shop_project_detail.domain.response.MyOrderResponse;
import com.example.shop_project_detail.exception.NotEnoughInventoryException;
import com.example.shop_project_detail.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("user")
public class UserPurchaseController {
    public OrderService orderService;
    public ProductService productService;
    public Order_ProductService order_productService;
    public LoginService loginService;
    public AsyncService asyncService;


    @Autowired
    public UserPurchaseController(OrderService orderService, ProductService productService, Order_ProductService order_productService, LoginService loginService, AsyncService asyncService) {
        this.orderService = orderService;
        this.productService = productService;
        this.order_productService = order_productService;
        this.loginService = loginService;
        this.asyncService = asyncService;
    }

    @PostMapping("/place_order")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) throws NotEnoughInventoryException {
        int order_id = orderService.findNextOrderId();
       // System.out.println(order_id);
        int user_id = request.getUser_id();

        String current_username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = loginService.getUserByUserId(user_id);
        if(user == null || !user.getUsername().equals(current_username)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Not Allowed");
        }

       // System.out.println(user_id);
        String order_status = "Processing";
        Timestamp date_placed = new Timestamp(System.currentTimeMillis());

        // check stock quantity
        for(int i = 0; i < request.getProductQuantities().size(); i++){
            int product_id = request.getProductQuantities().get(i).getProduct_id();
            int quantity = request.getProductQuantities().get(i).getQuantity();
            Product product = productService.getProductById(product_id);
            //System.out.println(product);
            if(quantity > product.getStock_quantity()){
                throw new NotEnoughInventoryException("Not enough inventory for item: " + product.getName());
            }
        }

        float profit = 0;

        // place order
        for(int i = 0; i < request.getProductQuantities().size(); i++) {
            int product_id = request.getProductQuantities().get(i).getProduct_id();
            int quantity = request.getProductQuantities().get(i).getQuantity();
            Product product = productService.getProductById(product_id);
            order_productService.addNewOrderProduct(order_id, product_id, quantity, product.getRetail_price(), product.getWholesale_price());
            profit = profit + quantity * (product.getRetail_price() - product.getWholesale_price());
            productService.updateQuantity(product_id, quantity);
        }

        orderService.placeOrder(user_id, order_status, date_placed, profit);


        return ResponseEntity.ok("Order Placed, Thanks!");
    }


    // get my placed order
    @GetMapping("/view_my_order")
    public ResponseEntity<List<MyOrderResponse>> getMyOrders(@RequestParam("user_id") int user_id){
        String current_username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = loginService.getUserByUserId(user_id);
        if(user == null || !user.getUsername().equals(current_username)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
        }
        System.out.println("executed here");
        return ResponseEntity.status(HttpStatus.OK).body(orderService.viewMyOrder(user_id));
    }


    // cancel an order
    @PostMapping("/cancel_order")
    public ResponseEntity<String> cancelOrder(@RequestParam("order_id") int order_id){
        String current_username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = orderService.findUserByOrderId(order_id);
        if(user == null || !user.getUsername().equals(current_username)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        Order order = orderService.findOrderByOrderId(order_id);
        if(order.getOrder_status().equals("Completed") || order.getOrder_status().equals("Canceled")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot cancel a Completed or Canceled order!");
        }
        orderService.cancelOrder(order_id);
        return ResponseEntity.ok().body("Order Canceled!");
    }

    // top 3 most frequently purchased items
    @GetMapping("/most_frequent_purchased")
    public ResponseEntity<List<MostPurchasedResponse>> getMostFrequentlyPurchased(@RequestParam("user_id") int user_id){
        String current_username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = loginService.getUserByUserId(user_id);
        if(user == null || !user.getUsername().equals(current_username)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
        }
        return ResponseEntity.ok().body(orderService.getMostFrequentlyPurchased(user_id));
    }

    // top 3 most recently purchased items
    @GetMapping("/most_recent_purchased")
    public ResponseEntity<List<MostPurchasedResponse>> getMostRecentlyPurchased(@RequestParam("user_id") int user_id){
        String current_username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = loginService.getUserByUserId(user_id);
        if(user == null || !user.getUsername().equals(current_username)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
        }
        return ResponseEntity.ok().body(orderService.getMostRecentlyPurchased(user_id));
    }

    @GetMapping("/{user_id}/order/{order_id}")
    public UserOrderResponse getOrderDetail(@PathVariable("user_id") int user_id, @PathVariable("order_id") int order_id){
        return orderService.getUserOrder(user_id, order_id);
    }

    @GetMapping("/async/{user_id}/order/{order_id}")
    public CompletableFuture<UserOrderResponse> getAsyncOrderDetail(@PathVariable("user_id") int user_id, @PathVariable("order_id") int order_id){
        return asyncService.getResponseAsync(user_id, order_id);
    }
}
