package com.example.shop_project_detail.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private int user_id;
    private List<ProductQuantity> productQuantities;
}
