package com.villan3ll3.estore.OrdersService.core.model;

import java.io.Serializable;

import com.villan3ll3.estore.OrdersService.core.data.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="orders")
@Data
public class OrderEntity implements Serializable {

    @Id
    @Column(unique = true)
    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
