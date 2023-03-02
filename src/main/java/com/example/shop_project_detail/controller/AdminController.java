package com.example.shop_project_detail.controller;


import com.example.shop_project_detail.domain.entity.Order;
import com.example.shop_project_detail.domain.entity.Product;
import com.example.shop_project_detail.domain.entity.User;
import com.example.shop_project_detail.domain.response.MostProfitProductResponse;
import com.example.shop_project_detail.domain.response.MostSoldProductsResponse;
import com.example.shop_project_detail.domain.response.OrderDetailResponse;
import com.example.shop_project_detail.domain.response.TopThreeBuyerResponse;
import com.example.shop_project_detail.service.OrderService;
import com.example.shop_project_detail.service.Order_ProductService;
import com.example.shop_project_detail.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {
    public OrderService orderService;
    public ProductService productService;
    public Order_ProductService order_productService;


    @Autowired
    public AdminController(OrderService orderService, ProductService productService, Order_ProductService order_productService) {
        this.orderService = orderService;
        this.productService = productService;
        this.order_productService = order_productService;
    }

    @PostMapping("/complete_order")
    public String completeAnOrder(@RequestParam("order_id") int order_id){
        Order order = orderService.findOrderByOrderId(order_id);
        if(order.getOrder_status().equals("Canceled") || order.getOrder_status().equals("Completed")){
            return "Cannot Complete a Canceled or Completed Order!";
        }
        orderService.completeOrder(order_id);
        return "Order Completed!";
    }

    @GetMapping("/get_all_orders")
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    // update a product
    @PostMapping("/update_product")
    public String updateProduct(@RequestBody Product product){
        productService.updateProduct(product);
        return "Updated Successfully";
    }

    @GetMapping("/view_order")
    public OrderDetailResponse getOrderByOrderId(@RequestParam("order_id") int order_id){
        return orderService.getOrderByOrderId(order_id);
    }

    @PostMapping("/cancel_order")
    public ResponseEntity<String> cancelOrder(@RequestParam("order_id") int order_id){
        orderService.cancelOrder(order_id);
        return ResponseEntity.ok().body("Order Canceled!");
    }

    @GetMapping("/most_profit_product")
    public MostProfitProductResponse getMostProfitProduct(){
        return order_productService.getMostProfitableProduct();
    }

    // top 3 most sold
    @GetMapping("/top_3_most_sold")
    public List<MostSoldProductsResponse> getTop3MostSold(){
        return order_productService.getTop3MostSoldProducts();
    }

    @GetMapping("/all_items_sold")
    public int getAllItemsSold(){
        return order_productService.getAllItemsSold();
    }

    @GetMapping("/top_3_buyer")
    public List<TopThreeBuyerResponse> getTop3Buyer(){
        return order_productService.getTop3Buyer();
    }

}
