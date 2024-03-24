package com.villan3ll3.estore.OrdersService.query;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.villan3ll3.estore.OrdersService.core.data.OrderSummary;
import com.villan3ll3.estore.OrdersService.core.data.OrdersRepository;
import com.villan3ll3.estore.OrdersService.core.model.OrderEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OrderQueriesHandler {

  private final OrdersRepository repository;

  @QueryHandler
  public OrderSummary findOrder(FindOrderQuery findOrderQuery) {

    OrderEntity orderEntity = repository.findByOrderId(findOrderQuery.getOrderId());
    return new OrderSummary(orderEntity.getOrderId(), orderEntity.getOrderStatus(), "");
  }
}
