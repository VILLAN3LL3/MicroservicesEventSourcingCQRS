package com.villan3ll3.estore.ProductsService.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.villan3ll3.estore.ProductsService.core.data.ProductEntity;
import com.villan3ll3.estore.ProductsService.core.data.ProductsRepository;
import com.villan3ll3.estore.ProductsService.core.events.ProductCreatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@ProcessingGroup("product-group")
public class ProductEventsHandler {

    private final ProductsRepository repository;

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalStateException exception) {
        // Log error message
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) {
        throw exception;
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        
        ProductEntity entity = new ProductEntity();
        BeanUtils.copyProperties(event, entity);

        repository.save(entity);
    }
}
