package com.example.shop_project_detail.domain.response;

import lombok.Data;

@Data
public class UserOrderResponse {
    private ServiceStatusResponse serviceStatus;
    private OrderResponse orderResponse;
    private UserResponse userResponse;
}
