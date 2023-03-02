package com.example.shop_project_detail.domain.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int product_id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "retail_price")
    private float retail_price;

    @Column(name = "wholesale_price")
    private float wholesale_price;

    @Column(name = "stock_quantity")
    private int stock_quantity;
}
