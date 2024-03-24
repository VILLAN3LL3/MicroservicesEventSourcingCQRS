package com.villan3ll3.estore.ProductsService.command;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.villan3ll3.estore.Core.commands.CancelProductReservationCommand;
import com.villan3ll3.estore.Core.commands.ReserveProductCommand;
import com.villan3ll3.estore.Core.events.ProductReservationCancelledEvent;
import com.villan3ll3.estore.Core.events.ProductReservedEvent;
import com.villan3ll3.estore.ProductsService.core.events.ProductCreatedEvent;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Aggregate(snapshotTriggerDefinition = "producSnapshotTriggerDefinition")
public class ProductAggregate {

  @AggregateIdentifier
  private String productId;
  private String title;
  private BigDecimal price;
  private Integer quantity;

  @CommandHandler
  public ProductAggregate(CreateProductCommand command) {

    ProductCreatedEvent event = new ProductCreatedEvent();
    BeanUtils.copyProperties(command, event);
    AggregateLifecycle.apply(event);
  }

  @CommandHandler
  public void handle(ReserveProductCommand reserveProductCommand) {

    if (quantity < reserveProductCommand.getQuantity()) {
      throw new IllegalArgumentException("Insufficient number of items in stock");
    }

    ProductReservedEvent productReservedEvent = ProductReservedEvent
        .builder()
        .orderId(reserveProductCommand.getOrderId())
        .productId(reserveProductCommand.getProductId())
        .userId(reserveProductCommand.getUserId())
        .quantity(reserveProductCommand.getQuantity())
        .build();

    AggregateLifecycle.apply(productReservedEvent);
  }

  @CommandHandler
  public void handle(CancelProductReservationCommand cancelProductReservationCommand) {
    
    ProductReservationCancelledEvent productReservationCancelledEvent = ProductReservationCancelledEvent
        .builder()
        .orderId(cancelProductReservationCommand.getOrderId())
        .productId(cancelProductReservationCommand.getProductId())
        .quantity(cancelProductReservationCommand.getQuantity())
        .reason(cancelProductReservationCommand.getReason())
        .userId(cancelProductReservationCommand.getUserId())
        .build();
    AggregateLifecycle.apply(productReservationCancelledEvent);
  }

  @EventSourcingHandler
  public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
    this.quantity += productReservationCancelledEvent.getQuantity();
  }

  @EventSourcingHandler
  public void on(ProductCreatedEvent event) {

    this.productId = event.getProductId();
    this.price = event.getPrice();
    this.title = event.getTitle();
    this.quantity = event.getQuantity();
  }

  @EventSourcingHandler
  public void on(ProductReservedEvent productReservedEvent) {

    this.quantity -= productReservedEvent.getQuantity();
  }
}
