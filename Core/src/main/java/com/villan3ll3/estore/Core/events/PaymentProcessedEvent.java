package com.villan3ll3.estore.Core.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class PaymentProcessedEvent {

  private final String orderId;
  private final String paymentId;
}
