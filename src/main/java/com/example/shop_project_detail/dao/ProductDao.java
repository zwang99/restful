package com.example.shop_project_detail.dao;



import com.example.shop_project_detail.domain.entity.Product;

import java.util.List;

public interface ProductDao {
    public void addNewProduct(String name, String description, float retail_price, float wholesale_price, int stock_quantity);
    public List<Product> getAllProducts();
    public Product getProductById(int product_id);
    public int getQuantityById(int product_id);
    public void updateQuantity(int product_id, int quantity);
    public void updateProduct(Product product);
}
