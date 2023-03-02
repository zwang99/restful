package com.example.shop_project_detail.controller;


import com.example.shop_project_detail.domain.entity.Product;
import com.example.shop_project_detail.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/addNewProduct")
    public ResponseEntity<String> addNewProduct(@RequestBody Product product){
        if(product.getDescription() == "" || product.getName() == "" || product.getRetail_price() == 0
        || product.getWholesale_price() == 0 || product.getStock_quantity() == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product information not complete");
        }

        productService.addNewProduct(product.getName(), product.getDescription(), product.getRetail_price(), product.getWholesale_price(),
                product.getStock_quantity());
        return ResponseEntity.ok("Add product successfully");
    }

    @GetMapping("/user/allProducts")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }


    @GetMapping("/user/product_detail")
    public Product getProductDetail(@RequestParam("product_id") int product_id){
        Product product = productService.getProductById(product_id);
        return product;
    }
}
