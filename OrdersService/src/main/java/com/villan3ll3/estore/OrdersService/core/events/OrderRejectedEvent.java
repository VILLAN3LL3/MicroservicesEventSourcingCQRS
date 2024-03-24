package com.villan3ll3.estore.OrdersService.core.events;

import com.villan3ll3.estore.OrdersService.core.data.OrderStatus;

import lombok.Value;

@Value
public class OrderRejectedEvent {

  private final String orderId;
  private final String reason;
  private final OrderStatus orderStatus = OrderStatus.REJECTED;
}
