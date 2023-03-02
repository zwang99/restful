package com.example.shop_project_detail.domain.response;

import com.example.shop_project_detail.domain.request.ProductQuantity;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class OrderResponse {
    private int orderId;
    private Timestamp time;
    private float totalPrice;
    List<ProductQuantity> orderItemResponseList;
}
