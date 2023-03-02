package com.example.shop_project_detail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
public class ShopProjectDetailApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopProjectDetailApplication.class, args);
    }

}
