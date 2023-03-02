package com.example.shop_project_detail.dao;

import com.example.shop_project_detail.domain.entity.Order_Product;
import com.example.shop_project_detail.domain.response.MostProfitProductResponse;
import com.example.shop_project_detail.domain.response.MostSoldProductsResponse;
import com.example.shop_project_detail.domain.response.TopThreeBuyerResponse;

import java.util.List;

public interface Order_ProductDao {
    public void addNewOrderProduct(int order_id, int product_id, int purchased_quantity, float execution_retail_price, float execution_wholesale_price);
    public List<Order_Product> findOrderProductByOrderId(int order_id);
    public void removeOrderProductByOrderProductId(int order_product_id);
    public MostProfitProductResponse getMostProfitableProduct();
    public List<MostSoldProductsResponse> getTop3MostSoldProducts();
    public int getAllItemsSold();
    public List<TopThreeBuyerResponse> getTop3Buyer();
}
