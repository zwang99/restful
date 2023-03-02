package com.example.shop_project_detail.domain.response;

import lombok.Data;

@Data
public class TopThreeBuyerResponse {
    private int user_id;
    private int money_spend;
}
