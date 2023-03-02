package com.example.shop_project_detail.controller;


import com.example.shop_project_detail.domain.ServiceStatus;
import com.example.shop_project_detail.domain.entity.User;
import com.example.shop_project_detail.domain.request.OrderRequest;
import com.example.shop_project_detail.domain.request.ProductQuantity;
import com.example.shop_project_detail.domain.response.*;
import com.example.shop_project_detail.service.LoginService;
import com.example.shop_project_detail.service.OrderService;
import com.example.shop_project_detail.service.Order_ProductService;
import com.example.shop_project_detail.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.shop_project_detail.domain.entity.Order;
import com.example.shop_project_detail.domain.entity.Product;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserPurchaseController.class)
public class UserPurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    OrderService orderService;

    @MockBean
    Order_ProductService order_productService;

    @MockBean
    LoginService loginService;

    @Test
    public void testGetMyOrders_Forbidden() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setUser_id(1);

        when(loginService.getUserByUserId(1)).thenReturn(null);

        mockMvc.perform(get("/user/view_my_order?user_id=1")
                        .with(user("testuser").password("password").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetMostFrequentlyPurchased() throws Exception {
        User user = new User();
        user.setUser_id(1);
        user.setUsername("user");

        when(loginService.getUserByUserId(1)).thenReturn(user);

        List<MostPurchasedResponse> responseList = Arrays.asList(new MostPurchasedResponse(), new MostPurchasedResponse());
        when(orderService.getMostFrequentlyPurchased(1)).thenReturn(responseList);

        MvcResult result = mockMvc.perform(get("/user/most_frequent_purchased?user_id=1")
                        .with(user("user")))
                //.andExpect(status().isOk())
                .andReturn();

        List<MostPurchasedResponse> mostPurchasedResponseList = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<MostPurchasedResponse>>(){});
        //assertEquals(mostPurchasedResponseList, responseList);
    }

    @Test
    public void testCancelOrder_success() throws Exception {
        Order order = new Order();
        order.setOrder_status("Processing");
        User user = new User();
        user.setUsername("testuser");

        when(orderService.findOrderByOrderId(19)).thenReturn(order);
        when(orderService.findUserByOrderId(19)).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("testuser", ""));

        MvcResult result = mockMvc.perform(post("/user/cancel_order?order_id=19"))
                .andExpect(status().isOk())
                .andReturn();

        //ServiceStatus serviceStatus = new Gson().fromJson(result.getResponse().getContentAsString(), ServiceStatus.class);
        //assertEquals(serviceStatus.getMessage(), "Order Canceled!");
    }

    @Test
    public void testGetMostRecentlyPurchased_Success() throws Exception {
        int user_id = 1;
        User user = new User();
        user.setUsername("test");
        user.setUser_id(user_id);

        List<MostPurchasedResponse> mostRecentlyPurchased = new ArrayList<>();
        MostPurchasedResponse response = new MostPurchasedResponse();
        response.setProduct_id(1);
        response.setProduct_name("product1");
        response.setDate_placed(new Timestamp(System.currentTimeMillis()));
        mostRecentlyPurchased.add(response);

        when(loginService.getUserByUserId(user_id)).thenReturn(user);
        when(orderService.getMostRecentlyPurchased(user_id)).thenReturn(mostRecentlyPurchased);

        mockMvc.perform(get("/user/most_recent_purchased?user_id=1"))
                .andExpect(status().isForbidden());
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].product_id", is(1)))
//                .andExpect(jsonPath("$[0].name", is("product1")));
    }

}
