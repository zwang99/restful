package com.example.shop_project_detail.service;


import com.example.shop_project_detail.dao.OrderDao;
import com.example.shop_project_detail.dao.OrderDaoImpl;
import com.example.shop_project_detail.dao.Order_ProductDao;
import com.example.shop_project_detail.domain.entity.Order;
import com.example.shop_project_detail.domain.response.MostPurchasedResponse;
import com.example.shop_project_detail.domain.response.OrderDetailResponse;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderDaoImpl orderDao;

    Order order1 = new Order();
    Order order2 = new Order();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setup1(){

        order1.setOrder_id(1);
        order1.setUser_id(1);
        order1.setOrder_status("Canceled");
        order1.setDate_placed(new Timestamp(System.currentTimeMillis()));
        order1.setProfit(0);

        order1.setOrder_id(2);
        order1.setUser_id(1);
        order1.setOrder_status("Completed");
        order1.setDate_placed(new Timestamp(System.currentTimeMillis()));
        order1.setProfit(300);
    }


    @Test
    public void testFindNextOrderId() {
        int nextOrderId = 0;
        try {
            nextOrderId = orderService.findNextOrderId();
        } catch (NullPointerException e) {
            fail("orderService is null");
        }
        assertFalse(nextOrderId > 0);
    }

    @Test
    public void testPlaceOrder() {
        int userId = 123;
        String orderStatus = "Processing";
        Timestamp datePlaced = new Timestamp(System.currentTimeMillis());
        float profit = 10.0f;
        orderService.placeOrder(userId, orderStatus, datePlaced, profit);
        verify(orderDao, times(1)).placeOrder(userId, orderStatus, datePlaced, profit);
    }

    @Test
    public void testFindOrderByUserId() {
        OrderDao orderDao = mock(OrderDao.class);
        OrderService orderService = new OrderService(orderDao);
        int userId = 1;
        List<Order> expectedOrders = new ArrayList<>();
        expectedOrders.add(order1);
        expectedOrders.add(order2);
        when(orderDao.findOrderByUserId(userId)).thenReturn(expectedOrders);
        List<Order> actualOrders = orderService.findOrderByUserId(userId);
        verify(orderDao, times(1)).findOrderByUserId(userId);
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void testCancelOrder() {
        int orderId = 1;
        orderService.cancelOrder(orderId);
        verify(orderDao, times(1)).cancelOrder(orderId);
    }

    @Test
    public void testGetOrderByOrderId() {
        int orderId = 1;
        OrderDetailResponse expectedResponse = new OrderDetailResponse();
        expectedResponse.setOrder_id(orderId);
        expectedResponse.setOrder_status("Processing");
        expectedResponse.setDate_placed(new Timestamp(System.currentTimeMillis()));
        when(orderDao.getOrderByOrderId(orderId)).thenReturn(expectedResponse);
        OrderDetailResponse actualResponse = orderService.getOrderByOrderId(orderId);
        verify(orderDao, times(1)).getOrderByOrderId(orderId);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetMostFrequentlyPurchased() {
        OrderDao orderDao = mock(OrderDao.class);
        OrderService orderService = new OrderService(orderDao);
        int userId = 123;
        List<MostPurchasedResponse> expectedResponses = new ArrayList<>();
        expectedResponses.add(new MostPurchasedResponse());
        expectedResponses.add(new MostPurchasedResponse());
        when(orderDao.getMostFrequentlyPurchased(userId)).thenReturn(expectedResponses);
        List<MostPurchasedResponse> actualResponses = orderService.getMostFrequentlyPurchased(userId);
        verify(orderDao, times(1)).getMostFrequentlyPurchased(userId);
        assertEquals(expectedResponses, actualResponses);
    }


}
