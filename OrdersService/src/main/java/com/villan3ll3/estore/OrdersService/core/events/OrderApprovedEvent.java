package com.villan3ll3.estore.OrdersService.core.events;

import com.villan3ll3.estore.OrdersService.core.data.OrderStatus;

import lombok.Value;

@Value
public class OrderApprovedEvent {

  private final String orderId;
  private final OrderStatus orderStatus = OrderStatus.APPROVED;
}
