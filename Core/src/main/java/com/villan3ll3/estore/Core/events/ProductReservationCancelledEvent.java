package com.villan3ll3.estore.Core.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReservationCancelledEvent {

  private final String productId;
  private final int quantity;
  private final String userId;
  private final String orderId;
  private final String reason;

}
