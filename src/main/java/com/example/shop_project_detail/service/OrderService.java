package com.example.shop_project_detail.service;


import com.example.shop_project_detail.dao.OrderDao;
import com.example.shop_project_detail.domain.entity.Order;
import com.example.shop_project_detail.domain.entity.User;
import com.example.shop_project_detail.domain.response.MostPurchasedResponse;
import com.example.shop_project_detail.domain.response.MyOrderResponse;
import com.example.shop_project_detail.domain.response.OrderDetailResponse;
import com.example.shop_project_detail.domain.response.UserOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService{
    public OrderDao orderDao;

    @Autowired
    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public int findNextOrderId() {
        return orderDao.findNextOrderId();
    }

    public void placeOrder(int user_id, String order_status, Timestamp date_placed, float profit) {
        orderDao.placeOrder(user_id, order_status, date_placed, profit);
    }
    public List<Order> findOrderByUserId(int user_id) {
        return orderDao.findOrderByUserId(user_id);
    }

    public Order findOrderByOrderId(int order_id) {
        return orderDao.findOrderByOrderId(order_id);
    }

    public void cancelOrder(int order_id) {
        orderDao.cancelOrder(order_id);
    }

    public void completeOrder(int order_id) {
        orderDao.completeOrder(order_id);
    }

    public List<MostPurchasedResponse> getMostFrequentlyPurchased(int user_id) {
        return orderDao.getMostFrequentlyPurchased(user_id);
    }

    public List<MostPurchasedResponse> getMostRecentlyPurchased(int user_id) {
        return orderDao.getMostRecentlyPurchased(user_id);
    }

    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }

    public OrderDetailResponse getOrderByOrderId(int order_id) {
        return orderDao.getOrderByOrderId(order_id);
    }

    public User findUserByOrderId(int order_id) {
        return orderDao.findUserByOrderId(order_id);
    }

    @Cacheable("my_order")
    public List<MyOrderResponse> viewMyOrder(int user_id) {
        return orderDao.viewMyOrder(user_id);
    }

    public UserOrderResponse getUserOrder(int user_id, int order_id) {
        return orderDao.getUserOrder(user_id, order_id);
    }

    @Async("taskExecutor")
    public CompletableFuture<Order> findOrderAsync(int order_id){
        Order order = orderDao.findOrderByOrderId(order_id);
        return CompletableFuture.completedFuture(order);
    }

}
