package com.example.shop_project_detail.dao;



import com.example.shop_project_detail.domain.entity.Product;

import java.util.List;

public interface WatchListDao {
    public void addToWatchList(int user_id, int product_id);
    public List<Product> getWatchlistByUserId(int user_id);
    public void removeFromWatchlist(int user_id, int product_id);
}
