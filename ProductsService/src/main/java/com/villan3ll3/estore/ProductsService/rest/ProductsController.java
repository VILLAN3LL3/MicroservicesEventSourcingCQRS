package com.villan3ll3.estore.ProductsService.rest;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.villan3ll3.estore.ProductsService.command.CreateProductCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductsController {

    private final Environment env;
    private final CommandGateway commandGateway;

    @PostMapping
    public String createProduct(@RequestBody CreateProductRestModel createProductRestModel) {
        
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

    @GetMapping
    public String getProduct() {
        return "HTTP GET Handled " + env.getProperty("local.server.port");
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
