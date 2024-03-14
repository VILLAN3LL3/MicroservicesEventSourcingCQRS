package com.villan3ll3.estore.OrdersService.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.villan3ll3.estore.OrdersService.core.data.OrdersRepository;
import com.villan3ll3.estore.OrdersService.core.events.OrderApprovedEvent;
import com.villan3ll3.estore.OrdersService.core.events.OrderCreatedEvent;
import com.villan3ll3.estore.OrdersService.core.model.OrderEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@ProcessingGroup("order-group")
public class OrderEventsHandler {

    private final OrdersRepository repository;

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalStateException exception) {
        // Log error message
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        
        OrderEntity entity = new OrderEntity();
        BeanUtils.copyProperties(event, entity);

        repository.save(entity);
    }

    @EventHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
      OrderEntity orderEntity = repository.findByOrderId(orderApprovedEvent.getOrderId());
      if(orderEntity == null) {
        // todo
        return;
      }
      orderEntity.setOrderStatus(orderApprovedEvent.getOrderStatus());
      repository.save(orderEntity);
    }
}
