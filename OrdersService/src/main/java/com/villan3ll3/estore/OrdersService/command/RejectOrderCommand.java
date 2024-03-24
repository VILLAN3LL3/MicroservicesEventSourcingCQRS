package com.villan3ll3.estore.OrdersService.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Value;

@Value
public class RejectOrderCommand {

  @TargetAggregateIdentifier
  private final String orderId;
  private final String reason;
}
