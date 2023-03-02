package com.example.shop_project_detail.domain.response;

import lombok.Data;

@Data
public class MostProfitProductResponse {
    private int product_id;
    private String prodcut_name;
    private float profit;
}
