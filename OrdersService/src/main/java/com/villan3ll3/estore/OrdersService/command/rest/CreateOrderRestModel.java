package com.villan3ll3.estore.OrdersService.command.rest;

import lombok.Data;

@Data
public class CreateOrderRestModel {

    private Integer quantity;
    private String addressId;
    private String productId;
}
