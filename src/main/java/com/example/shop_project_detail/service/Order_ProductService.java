package com.example.shop_project_detail.service;


import com.example.shop_project_detail.dao.Order_ProductDao;
import com.example.shop_project_detail.domain.entity.Order;
import com.example.shop_project_detail.domain.entity.Order_Product;
import com.example.shop_project_detail.domain.response.MostProfitProductResponse;
import com.example.shop_project_detail.domain.response.MostSoldProductsResponse;
import com.example.shop_project_detail.domain.response.TopThreeBuyerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class Order_ProductService {
    public Order_ProductDao order_productDao;

    @Autowired
    public Order_ProductService(Order_ProductDao order_productDao) {
        this.order_productDao = order_productDao;
    }

    public void addNewOrderProduct(int order_id, int product_id, int purchased_quantity, float execution_retail_price, float execution_wholesale_price) {
        order_productDao.addNewOrderProduct(order_id, product_id, purchased_quantity, execution_retail_price, execution_wholesale_price);
    }

    public List<Order_Product> findOrderProductByOrderId(int order_id) {
        return order_productDao.findOrderProductByOrderId(order_id);
    }
    public void removeOrderProductByOrderProductId(int order_product_id) {
        order_productDao.removeOrderProductByOrderProductId(order_product_id);
    }

    public MostProfitProductResponse getMostProfitableProduct() {
        return order_productDao.getMostProfitableProduct();
    }

    public List<MostSoldProductsResponse> getTop3MostSoldProducts() {
        return order_productDao.getTop3MostSoldProducts();
    }

    public int getAllItemsSold() {
        return order_productDao.getAllItemsSold();
    }

    public List<TopThreeBuyerResponse> getTop3Buyer() {
        return order_productDao.getTop3Buyer();
    }

    @Async("taskExecutor")
    public CompletableFuture<List<Order_Product>> findOrderProductAsync(int order_id){
        List<Order_Product> order_products = order_productDao.findOrderProductByOrderId(order_id);
        return CompletableFuture.completedFuture(order_products);
    }
}
