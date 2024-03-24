package com.villan3ll3.estore.OrdersService.core.data;

import lombok.Value;

@Value
public class OrderSummary {

  private final String orderId;
  private final OrderStatus orderStatus;
  private final String message;
}
