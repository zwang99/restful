package com.example.shop_project_detail.domain.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MostPurchasedResponse {
    private int product_id;
    private String product_name;
    private int quantity;
    private Timestamp date_placed;
}
