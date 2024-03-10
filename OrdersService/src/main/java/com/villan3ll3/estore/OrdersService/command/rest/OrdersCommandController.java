package com.villan3ll3.estore.OrdersService.command.rest;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.villan3ll3.estore.OrdersService.command.CreateOrderCommand;
import com.villan3ll3.estore.OrdersService.core.data.OrderStatus;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrdersCommandController {

    private final CommandGateway commandGateway;

    @PostMapping
    public String createOrder(@Valid @RequestBody CreateOrderRestModel createOrderRestModel) {

        CreateOrderCommand command = CreateOrderCommand
                .builder()
                .addressId(createOrderRestModel.getAddressId())
                .quantity(createOrderRestModel.getQuantity())
                .productId(createOrderRestModel.getProductId())
                .orderId(UUID.randomUUID().toString())
                .userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
                .orderStatus(OrderStatus.CREATED)
                .build();

        return commandGateway.sendAndWait(command);
    }
}
