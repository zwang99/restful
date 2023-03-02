package com.example.shop_project_detail.domain.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Order_Product")
public class Order_Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private int order_product_id;

    @Column(name = "order_id")
    private int order_id;

    @Column(name = "product_id")
    private int product_id;

    @Column(name = "purchased_quantity")
    private int purchased_quantity;

    @Column(name = "execution_retail_price")
    private float execution_retail_price;

    @Column(name = "execution_wholesale_price")
    private float execution_wholesale_price;

}
