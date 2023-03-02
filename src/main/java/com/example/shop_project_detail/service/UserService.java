package com.example.shop_project_detail.service;

import com.example.shop_project_detail.dao.LoginDao;
import com.example.shop_project_detail.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {
    LoginDao loginDao;

    @Autowired
    public UserService(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<User> getUserAsync(int user_id){
        User user = loginDao.getUserByUserId(user_id);
        return CompletableFuture.completedFuture(user);
    }
}
