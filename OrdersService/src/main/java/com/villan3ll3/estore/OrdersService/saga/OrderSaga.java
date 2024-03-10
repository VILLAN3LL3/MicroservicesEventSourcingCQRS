package com.villan3ll3.estore.OrdersService.saga;

import javax.annotation.Nonnull;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.villan3ll3.estore.Core.commands.ReserveProductCommand;
import com.villan3ll3.estore.Core.events.ProductReservedEvent;
import com.villan3ll3.estore.OrdersService.core.events.OrderCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Saga
public class OrderSaga {

    private final transient CommandGateway commandGateway;

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

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent) {

        // Process user payment
        log.info(
            "ProductReservedEvent is called for productId: {} and orderId: {}", 
            productReservedEvent.getProductId() +
            productReservedEvent.getOrderId());
    }
}
