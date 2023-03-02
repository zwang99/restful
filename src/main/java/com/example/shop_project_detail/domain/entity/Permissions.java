package com.example.shop_project_detail.domain.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Permissions")
@Data
public class Permissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id", unique = true, nullable = false)
    private int permission_id;

    @Column(name = "permission")
    private String permission;
}
