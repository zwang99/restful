package com.example.shop_project_detail.domain.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "OrderTable")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int order_id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "order_status")
    private String order_status;

    @Column(name = "date_placed")
    private Timestamp date_placed;

    @Column(name = "profit")
    private float profit;
}
