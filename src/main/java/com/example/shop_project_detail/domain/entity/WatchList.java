package com.example.shop_project_detail.domain.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="Product_Watch_List")
@Data
public class WatchList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int watch_list_id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "product_id")
    private int product_id;
}
