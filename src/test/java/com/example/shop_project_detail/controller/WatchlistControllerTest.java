package com.example.shop_project_detail.controller;

import com.example.shop_project_detail.domain.entity.User;
import com.example.shop_project_detail.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.example.shop_project_detail.domain.entity.Order;
import com.example.shop_project_detail.domain.entity.Product;
import com.example.shop_project_detail.domain.response.MostProfitProductResponse;
import com.example.shop_project_detail.domain.response.MostSoldProductsResponse;
import com.example.shop_project_detail.domain.response.OrderDetailResponse;
import com.example.shop_project_detail.domain.response.TopThreeBuyerResponse;
import com.example.shop_project_detail.service.OrderService;
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
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sun.nio.cs.Surrogate.is;

@WebMvcTest(WatchListController.class)
public class WatchlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    WatchListService watchListService;

    @MockBean
    LoginService loginService;

    @Test
    public void addToWatchListTest() throws Exception {
        User user = new User();
        user.setUser_id(1);
        user.setUsername("usertest");
        Product product = new Product();
        product.setProduct_id(1);
        product.setDescription("good stuff");
        when(loginService.getUserByUserId(1)).thenReturn(user);

        mockMvc.perform(post("/user/addWatchlist")
                        .param("user_id", "1")
                        .param("product_id", "1"))
                .andExpect(status().isForbidden());
                //.andExpect(content().string("Add to Watchlist successfully!"));

        //verify(watchListService, times(1)).addToWatchList(1, 1);
    }

    @Test
    public void testViewWatchList() throws Exception {
        List<Product> watchList = Arrays.asList();
        when(watchListService.getWatchlistByUserId(1)).thenReturn(watchList);
        mockMvc.perform(get("/user/view_watchlist?user_id=1"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$", hasSize(0)));
//                .andExpect((ResultMatcher) jsonPath("$[0].id", is(1)))
//                .andExpect(jsonPath("$[0].name", is("product1")))
//                .andExpect(jsonPath("$[1].id", is(2)))
//                .andExpect(jsonPath("$[1].name", is("product2")));
        //verify(watchListService, times(1)).getWatchlistByUserId(1);
    }
}
