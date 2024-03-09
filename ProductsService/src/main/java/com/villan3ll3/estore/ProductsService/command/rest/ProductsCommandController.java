package com.villan3ll3.estore.ProductsService.command.rest;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.villan3ll3.estore.ProductsService.command.CreateProductCommand;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductsCommandController {

    private final CommandGateway commandGateway;

    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductRestModel createProductRestModel) {
        
        CreateProductCommand command = CreateProductCommand
            .builder()
            .price(createProductRestModel.getPrice())
            .quantity(createProductRestModel.getQuantity())
            .title(createProductRestModel.getTitle())
            .productId(UUID.randomUUID().toString())
            .build();
        
        String returnValue;
        try {
            returnValue = commandGateway.sendAndWait(command);
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
        
        return returnValue;
    }

    @PutMapping
    public String updateProduct() {
        return "HTTP PUT Handled";
    }

    @DeleteMapping
    public String deleteProduct() {
        return "HTTP DELETE Handled";
    }
}
