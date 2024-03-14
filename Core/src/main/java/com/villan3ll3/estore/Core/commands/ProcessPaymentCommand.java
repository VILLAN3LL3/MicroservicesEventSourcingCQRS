package com.villan3ll3.estore.Core.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.villan3ll3.estore.Core.model.PaymentDetails;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessPaymentCommand {

  @TargetAggregateIdentifier
  private final String paymentId;
  private final String orderId;
  private final PaymentDetails paymentDetails;

}
