package com.example.shop_project_detail.domain.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "User_Permission")
@Data
public class User_Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_permission_id", unique = true, nullable = false)
    private int user_permission_id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "permission_id")
    private int permission_id;
}
