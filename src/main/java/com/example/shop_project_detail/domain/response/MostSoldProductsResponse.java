package com.example.shop_project_detail.domain.response;

import lombok.Data;

@Data
public class MostSoldProductsResponse {
    private int product_id;
    private String product_name;
    private int sold_quantity;
}
