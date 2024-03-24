package com.villan3ll3.estore.OrdersService.saga;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;

import com.villan3ll3.estore.Core.commands.CancelProductReservationCommand;
import com.villan3ll3.estore.Core.commands.ProcessPaymentCommand;
import com.villan3ll3.estore.Core.commands.ReserveProductCommand;
import com.villan3ll3.estore.Core.events.PaymentProcessedEvent;
import com.villan3ll3.estore.Core.events.ProductReservationCancelledEvent;
import com.villan3ll3.estore.Core.events.ProductReservedEvent;
import com.villan3ll3.estore.Core.model.User;
import com.villan3ll3.estore.Core.query.FetchUserPaymentDetailsQuery;
import com.villan3ll3.estore.OrdersService.command.ApproveOrderCommand;
import com.villan3ll3.estore.OrdersService.command.RejectOrderCommand;
import com.villan3ll3.estore.OrdersService.core.events.OrderApprovedEvent;
import com.villan3ll3.estore.OrdersService.core.events.OrderCreatedEvent;
import com.villan3ll3.estore.OrdersService.core.events.OrderRejectedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Saga
public class OrderSaga {

  private final transient CommandGateway commandGateway;
  private final transient QueryGateway queryGateway;

  @StartSaga
  @SagaEventHandler(associationProperty = "orderId")
  public void handle(OrderCreatedEvent event) {

    ReserveProductCommand reserveProductCommand = ReserveProductCommand
        .builder()
        .orderId(event.getOrderId())
        .productId(event.getProductId())
        .quantity(event.getQuantity())
        .userId(event.getUserId())
        .build();

    log.info(
        "OrderCreatedEvent handled for orderId: {} and productId: {}",
        reserveProductCommand.getOrderId(),
        reserveProductCommand.getProductId());

    commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {

      @Override
      public void onResult(@Nonnull CommandMessage<? extends ReserveProductCommand> commandMessage,
          @Nonnull CommandResultMessage<? extends Object> commandResultMessage) {

        if (commandResultMessage.isExceptional()) {
          // Start a compensating transaction
        }
      }
    });
  }

  /**
   * @param productReservedEvent
   */
  @SagaEventHandler(associationProperty = "orderId")
  public void handle(ProductReservedEvent productReservedEvent) {

    // Process user payment
    log.info(
        "ProductReservedEvent is called for productId: {} and orderId: {}",
        productReservedEvent.getProductId() +
            productReservedEvent.getOrderId());

    FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(
        productReservedEvent.getUserId());

    User userPaymentDetails = null;

    try {
      userPaymentDetails = queryGateway
          .query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class))
          .join();
    } catch (Exception e) {
      log.error(e.getMessage());
      cancelProductReservation(productReservedEvent, e.getMessage());
      return;
    }

    if (userPaymentDetails == null) {
      cancelProductReservation(productReservedEvent, "Could not fetch user payment details");
      return;
    }

    log.info("Successfully fetched user payment details for user {}", userPaymentDetails.getFirstName());

    ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand
        .builder()
        .orderId(productReservedEvent.getOrderId())
        .paymentDetails(userPaymentDetails.getPaymentDetails())
        .paymentId(UUID.randomUUID().toString())
        .build();

    String result = null;

    try {
      result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
    } catch (Exception e) {
      log.error(e.getMessage());
      cancelProductReservation(productReservedEvent, e.getMessage());
    }

    if (result == null) {
      log.info("The ProcessPaymentCommand resulted in NULL. Initiating a compensating transaction");
      cancelProductReservation(productReservedEvent, "Could not process user payment with provided payment details");
    }
  }

  @SagaEventHandler(associationProperty = "orderId")
  public void handle(PaymentProcessedEvent paymentProcessedEvent) {
    ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
    commandGateway.send(approveOrderCommand);
  }

  @EndSaga
  @SagaEventHandler(associationProperty = "orderId")
  public void handle(OrderApprovedEvent orderApprovedEvent) {
    log.info("Order is approved. Order Saga is complete for orderId: {}", orderApprovedEvent.getOrderId());
  }

  @SagaEventHandler(associationProperty = "orderId")
  public void handle(ProductReservationCancelledEvent productReservationCancelledEvent) {
    RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(
        productReservationCancelledEvent.getOrderId(),
        productReservationCancelledEvent.getReason());
    commandGateway.send(rejectOrderCommand);
  }

  @EndSaga
  @SagaEventHandler(associationProperty = "orderId")
  public void handle(OrderRejectedEvent orderRejectedEvent) {
    log.info("Successfully rejected order with id {}", orderRejectedEvent.getOrderId());
  }

  private void cancelProductReservation(ProductReservedEvent productReservedEvent, String reason) {
    CancelProductReservationCommand cancelProductReservationCommand = CancelProductReservationCommand
        .builder()
        .orderId(productReservedEvent.getOrderId())
        .productId(productReservedEvent.getProductId())
        .quantity(productReservedEvent.getQuantity())
        .userId(productReservedEvent.getUserId())
        .reason(reason)
        .build();
    commandGateway.send(cancelProductReservationCommand);
  }
}
