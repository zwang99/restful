package com.example.shop_project_detail.service;


import com.example.shop_project_detail.dao.OrderDao;
import com.example.shop_project_detail.dao.WatchListDao;
import com.example.shop_project_detail.domain.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WatchlistServiceTest {
    @InjectMocks
    WatchListService watchListService;

    @Mock
    WatchListDao watchListDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addToWatchListTest() {

        // call the addToWatchList method
        int user_id = 1;
        int product_id = 2;
        watchListService.addToWatchList(user_id, product_id);

        // verify that the addToWatchList method was called on the watchListDao object
        verify(watchListDao).addToWatchList(user_id, product_id);
    }

    @Test
    public void getWatchlistByUserIdTest() {
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(new Product());
        expectedProducts.add(new Product());

        Mockito.when(watchListDao.getWatchlistByUserId(1)).thenReturn(expectedProducts);

        List<Product> actualProducts = watchListService.getWatchlistByUserId(1);
        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    public void removeFromWatchlistTest() {
        int user_id = 1;
        int product_id = 2;

        // Add a product to the watchlist before removing it
        watchListService.addToWatchList(user_id, product_id);

        // Remove the product from the watchlist
        watchListService.removeFromWatchlist(user_id, product_id);

        // Get the watchlist for the user
        List<Product> watchlist = watchListService.getWatchlistByUserId(user_id);

        // Check if the product has been removed from the watchlist
        assertEquals(0, watchlist.size());
    }

}
