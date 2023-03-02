package com.example.shop_project_detail.service;


import com.example.shop_project_detail.dao.ProductDao;
import com.example.shop_project_detail.domain.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    ProductDao productDao;

    @Autowired
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void addNewProduct(String name, String description, float retail_price, float wholesale_price, int stock_quantity) {
        productDao.addNewProduct(name, description, retail_price, wholesale_price, stock_quantity);
    }
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public Product getProductById(int product_id) {
        return productDao.getProductById(product_id);
    }

    public int getQuantityById(int product_id) {
        return productDao.getQuantityById(product_id);
    }

    public void updateQuantity(int product_id, int quantity) {
        productDao.updateQuantity(product_id, quantity);
    }

    public void updateProduct(Product product) {
        productDao.updateProduct(product);
    }
}
