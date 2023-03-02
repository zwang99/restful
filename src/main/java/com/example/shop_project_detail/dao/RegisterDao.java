package com.example.shop_project_detail.dao;


import com.example.shop_project_detail.domain.entity.User;

public interface RegisterDao {
    public User findUserByUsername(String username);
    public User findUserByEmail(String email);
    public void saveUser(User user);
    public void addPermissions(int user_id);
}
