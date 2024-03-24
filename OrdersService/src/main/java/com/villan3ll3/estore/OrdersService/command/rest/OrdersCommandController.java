package com.villan3ll3.estore.OrdersService.command.rest;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.villan3ll3.estore.OrdersService.command.CreateOrderCommand;
import com.villan3ll3.estore.OrdersService.core.data.OrderStatus;
import com.villan3ll3.estore.OrdersService.core.data.OrderSummary;
import com.villan3ll3.estore.OrdersService.query.FindOrderQuery;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrdersCommandController {

  private final CommandGateway commandGateway;
  private final QueryGateway queryGateway;

  @PostMapping
  public OrderSummary createOrder(@Valid @RequestBody CreateOrderRestModel createOrderRestModel) {

    String orderId = UUID.randomUUID().toString();

    CreateOrderCommand command = CreateOrderCommand
        .builder()
        .addressId(createOrderRestModel.getAddressId())
        .quantity(createOrderRestModel.getQuantity())
        .productId(createOrderRestModel.getProductId())
        .orderId(orderId)
        .userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
        .orderStatus(OrderStatus.CREATED)
        .build();

    SubscriptionQueryResult<OrderSummary, OrderSummary> queryResult = queryGateway.subscriptionQuery(
        new FindOrderQuery(orderId),
        ResponseTypes.instanceOf(OrderSummary.class),
        ResponseTypes.instanceOf(OrderSummary.class));

    try {
      commandGateway.sendAndWait(command);
    } finally {
      queryResult.close();
    }

    return queryResult.updates().blockFirst();
  }
}
