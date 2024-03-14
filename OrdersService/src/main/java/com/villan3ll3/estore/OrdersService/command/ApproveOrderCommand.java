package com.villan3ll3.estore.OrdersService.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApproveOrderCommand {

  @TargetAggregateIdentifier
  private final String orderId;

}
