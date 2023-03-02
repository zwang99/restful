package com.example.shop_project_detail.controller;


import com.example.shop_project_detail.domain.ServiceStatus;
import com.example.shop_project_detail.domain.entity.Order;
import com.example.shop_project_detail.domain.entity.Product;
import com.example.shop_project_detail.domain.response.MostProfitProductResponse;
import com.example.shop_project_detail.domain.response.MostSoldProductsResponse;
import com.example.shop_project_detail.domain.response.OrderDetailResponse;
import com.example.shop_project_detail.domain.response.TopThreeBuyerResponse;
import com.example.shop_project_detail.service.OrderService;
import com.example.shop_project_detail.service.Order_ProductService;
import com.example.shop_project_detail.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.test.web.servlet.ResultMatcher;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    ProductService productService;

    @MockBean
    Order_ProductService order_productService;

    @Test
    public void testCompleteOrder_success() throws Exception {
        Order order = new Order();
        order.setOrder_status("Processing");

        when(orderService.findOrderByOrderId(19)).thenReturn(order);

        mockMvc.perform(post("/admin/complete_order?order_id=19"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order Completed!"));
    }

    @Test
    public void testCompleteOrder_fail() throws Exception {
        Order order = new Order();
        order.setOrder_status("Canceled");

        when(orderService.findOrderByOrderId(19)).thenReturn(order);

        mockMvc.perform(post("/admin/complete_order?order_id=19"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cannot Complete a Canceled or Completed Order!"));
    }

    @Test
    public void testGetAllOrders() throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());

        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/admin/get_all_orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product();
        product.setProduct_id(1);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setRetail_price((float) 100.0);

        mockMvc.perform(post("/admin/update_product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated Successfully"));

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productService, times(1)).updateProduct(captor.capture());
        assertEquals(product.getProduct_id(), captor.getValue().getProduct_id());
        assertEquals(product.getName(), captor.getValue().getName());
        assertEquals(product.getDescription(), captor.getValue().getDescription());
        assertEquals(product.getRetail_price(), captor.getValue().getRetail_price(), 0.0);
    }

    @Test
    public void testGetOrderByOrderId() throws Exception {
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        orderDetailResponse.setOrder_id(1);
        orderDetailResponse.setOrder_status("Processing");
        orderDetailResponse.setDate_placed(new Timestamp(System.currentTimeMillis()));

        when(orderService.getOrderByOrderId(1)).thenReturn(orderDetailResponse);

        MvcResult result = mockMvc.perform(get("/admin/view_order?order_id=1"))
                .andExpect(status().isOk())
                .andReturn();

        OrderDetailResponse response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), OrderDetailResponse.class);
        assertEquals(orderDetailResponse.getOrder_id(), response.getOrder_id());
        assertEquals(orderDetailResponse.getOrder_status(), response.getOrder_status());
        assertEquals(orderDetailResponse.getDate_placed(), response.getDate_placed());

        verify(orderService, times(1)).getOrderByOrderId(1);
    }

    @Test
    public void testCancelOrder() throws Exception {
        mockMvc.perform(post("/admin/cancel_order?order_id=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order Canceled!"));

        verify(orderService, times(1)).cancelOrder(1);
    }

    @Test
    public void testGetMostProfitableProduct() throws Exception {
        MostProfitProductResponse mostProfitProductResponse = new MostProfitProductResponse();
        mostProfitProductResponse.setProduct_id(1);
        mostProfitProductResponse.setProdcut_name("Product 1");
        mostProfitProductResponse.setProfit((float) 1000.0);

        when(order_productService.getMostProfitableProduct()).thenReturn(mostProfitProductResponse);

        MvcResult result = mockMvc.perform(get("/admin/most_profit_product"))
                .andExpect(status().isOk())
                .andReturn();

        MostProfitProductResponse response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), MostProfitProductResponse.class);
        assertEquals(mostProfitProductResponse.getProduct_id(), response.getProduct_id());
        assertEquals(mostProfitProductResponse.getProdcut_name(), response.getProdcut_name());
        assertEquals(mostProfitProductResponse.getProfit(), response.getProfit(), 0.0);

        verify(order_productService, times(1)).getMostProfitableProduct();
    }

    @Test
    public void testGetTop3MostSoldProducts() throws Exception {
        List<MostSoldProductsResponse> mostSoldProductsResponses = new ArrayList<>();
        MostSoldProductsResponse mostSoldProductsResponse1 = new MostSoldProductsResponse();
        mostSoldProductsResponse1.setProduct_id(1);
        mostSoldProductsResponse1.setProduct_name("Product 1");
        mostSoldProductsResponse1.setSold_quantity(10);
        mostSoldProductsResponses.add(mostSoldProductsResponse1);

        MostSoldProductsResponse mostSoldProductsResponse2 = new MostSoldProductsResponse();
        mostSoldProductsResponse2.setProduct_id(2);
        mostSoldProductsResponse2.setProduct_name("Product 2");
        mostSoldProductsResponse2.setSold_quantity(20);
        mostSoldProductsResponses.add(mostSoldProductsResponse2);

        MostSoldProductsResponse mostSoldProductsResponse3 = new MostSoldProductsResponse();
        mostSoldProductsResponse3.setProduct_id(3);
        mostSoldProductsResponse3.setProduct_name("Product 3");
        mostSoldProductsResponse3.setSold_quantity(30);
        mostSoldProductsResponses.add(mostSoldProductsResponse3);

        when(order_productService.getTop3MostSoldProducts()).thenReturn(mostSoldProductsResponses);

        MvcResult result = mockMvc.perform(get("/admin/top_3_most_sold"))
                .andExpect(status().isOk())
                .andReturn();

        List<MostSoldProductsResponse> response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<MostSoldProductsResponse>>() {
        });
        assertEquals(mostSoldProductsResponses.size(), response.size());
        for (int i = 0; i < mostSoldProductsResponses.size(); i++) {
            assertEquals(mostSoldProductsResponses.get(i).getProduct_id(), response.get(i).getProduct_id());
            assertEquals(mostSoldProductsResponses.get(i).getProduct_name(), response.get(i).getProduct_name());
            assertEquals(mostSoldProductsResponses.get(i).getSold_quantity(), response.get(i).getSold_quantity());
        }

        verify(order_productService, times(1)).getTop3MostSoldProducts();
    }

    @Test
    public void testGetAllItemsSold() throws Exception {
        int allItemsSold = 100;
        when(order_productService.getAllItemsSold()).thenReturn(allItemsSold);

        MvcResult result = mockMvc.perform(get("/admin/all_items_sold"))
                .andExpect(status().isOk())
                .andReturn();

        int response = Integer.parseInt(result.getResponse().getContentAsString());
        assertEquals(allItemsSold, response);

        verify(order_productService, times(1)).getAllItemsSold();
    }

    @Test
    public void testGetTop3Buyer() throws Exception {
        List<TopThreeBuyerResponse> topThreeBuyerResponses = new ArrayList<>();
        TopThreeBuyerResponse topThreeBuyerResponse1 = new TopThreeBuyerResponse();
        topThreeBuyerResponse1.setUser_id(1);
        topThreeBuyerResponse1.setMoney_spend(100);
        topThreeBuyerResponses.add(topThreeBuyerResponse1);

        TopThreeBuyerResponse topThreeBuyerResponse2 = new TopThreeBuyerResponse();
        topThreeBuyerResponse2.setUser_id(2);
        topThreeBuyerResponse2.setMoney_spend(200);
        topThreeBuyerResponses.add(topThreeBuyerResponse2);

        TopThreeBuyerResponse topThreeBuyerResponse3 = new TopThreeBuyerResponse();
        topThreeBuyerResponse3.setUser_id(3);
        topThreeBuyerResponse3.setMoney_spend(300);
        topThreeBuyerResponses.add(topThreeBuyerResponse3);

        when(order_productService.getTop3Buyer()).thenReturn(topThreeBuyerResponses);

        MvcResult result = mockMvc.perform(get("/admin/top_3_buyer"))
                .andExpect(status().isOk())
                .andReturn();

        List<TopThreeBuyerResponse> response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<TopThreeBuyerResponse>>() {
        });
        assertEquals(topThreeBuyerResponses.size(), response.size());
        for (int i = 0; i < topThreeBuyerResponses.size(); i++) {
            assertEquals(topThreeBuyerResponses.get(i).getUser_id(), response.get(i).getUser_id());
            assertEquals(topThreeBuyerResponses.get(i).getMoney_spend(), response.get(i).getMoney_spend());
        }
        verify(order_productService, times(1)).getTop3Buyer();
    }
}
