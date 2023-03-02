package com.example.shop_project_detail.dao;

import com.example.shop_project_detail.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface LoginDao {
    // Optional<User> findUserByUsernameAndPassword(String username, String password);
    public Optional<User> loadUserByUsername(String username);
    public User getUserByUserId(int user_id);
    List<String> getPermissionsByUserId(int user_id);
}
