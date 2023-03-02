package com.example.shop_project_detail.service;

import com.example.shop_project_detail.dao.WatchListDao;
import com.example.shop_project_detail.domain.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchListService {
    public WatchListDao watchListDao;

    @Autowired
    public WatchListService(WatchListDao watchListDao) {
        this.watchListDao = watchListDao;
    }

    public void addToWatchList(int user_id, int product_id) {
        watchListDao.addToWatchList(user_id, product_id);
    }

    public List<Product> getWatchlistByUserId(int user_id) {
        return watchListDao.getWatchlistByUserId(user_id);
    }

    public void removeFromWatchlist(int user_id, int product_id) {
        watchListDao.removeFromWatchlist(user_id, product_id);
    }
}
