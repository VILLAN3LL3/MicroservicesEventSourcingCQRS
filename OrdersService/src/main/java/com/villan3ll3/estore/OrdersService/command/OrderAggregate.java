package com.villan3ll3.estore.OrdersService.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.villan3ll3.estore.OrdersService.core.data.OrderStatus;
import com.villan3ll3.estore.OrdersService.core.events.OrderApprovedEvent;
import com.villan3ll3.estore.OrdersService.core.events.OrderCreatedEvent;
import com.villan3ll3.estore.OrdersService.core.events.OrderRejectedEvent;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.userId = event.getUserId();
        this.productId = event.getProductId();
        this.quantity = event.getQuantity();
        this.addressId = event.getAddressId();
        this.orderStatus = event.getOrderStatus();
    }

    @CommandHandler
    public void handle(ApproveOrderCommand approveOrderCommand) {
      
      OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(approveOrderCommand.getOrderId());
      AggregateLifecycle.apply(orderApprovedEvent);
    }

    @EventSourcingHandler
    protected void on(OrderApprovedEvent orderApprovedEvent) {
      this.orderStatus = orderApprovedEvent.getOrderStatus();
    }

    @CommandHandler
    public void handle(RejectOrderCommand rejectOrderCommand) {
      OrderRejectedEvent orderRejectedEvent = new OrderRejectedEvent(
        rejectOrderCommand.getOrderId(), 
        rejectOrderCommand.getReason());
        AggregateLifecycle.apply(orderRejectedEvent);
    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent orderRejectedEvent) {
      this.orderStatus = orderRejectedEvent.getOrderStatus();
    }
}
