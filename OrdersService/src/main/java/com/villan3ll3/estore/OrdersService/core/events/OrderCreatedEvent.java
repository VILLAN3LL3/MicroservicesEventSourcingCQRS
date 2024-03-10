package com.villan3ll3.estore.OrdersService.core.events;

import com.villan3ll3.estore.OrdersService.core.data.OrderStatus;

import lombok.Data;

@Data
public class OrderCreatedEvent {

    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;
}
