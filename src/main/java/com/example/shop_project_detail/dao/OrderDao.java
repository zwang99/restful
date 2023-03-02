package com.example.shop_project_detail.dao;



import com.example.shop_project_detail.domain.entity.Order;
import com.example.shop_project_detail.domain.entity.User;
import com.example.shop_project_detail.domain.response.MostPurchasedResponse;
import com.example.shop_project_detail.domain.response.MyOrderResponse;
import com.example.shop_project_detail.domain.response.OrderDetailResponse;
import com.example.shop_project_detail.domain.response.UserOrderResponse;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderDao {
    public int findNextOrderId();
    public void placeOrder(int user_id, String order_status, Timestamp date_placed, float profit);
    public List<Order> findOrderByUserId(int user_id);
    public Order findOrderByOrderId(int order_id);
    public List<MyOrderResponse> viewMyOrder(int user_id);
    public void cancelOrder(int order_id);
    public void completeOrder(int order_id);
    public List<MostPurchasedResponse> getMostFrequentlyPurchased(int user_id);
    public List<MostPurchasedResponse> getMostRecentlyPurchased(int user_id);
    public List<Order> getAllOrders();
    public OrderDetailResponse getOrderByOrderId(int order_id);
    public User findUserByOrderId(int order_id);
    public UserOrderResponse getUserOrder(int user_id, int order_id);

}
