package com.example.shop_project_detail.domain.response;

import com.example.shop_project_detail.domain.request.ProductQuantity;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class MyOrderResponse {
    private int order_id;
    private String order_status;
    private Timestamp date_placed;
    List<ProductQuantity> productQuantityList;
}
