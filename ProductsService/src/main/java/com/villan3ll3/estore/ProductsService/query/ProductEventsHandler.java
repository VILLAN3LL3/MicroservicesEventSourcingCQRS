package com.villan3ll3.estore.ProductsService.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.villan3ll3.estore.Core.events.ProductReservedEvent;
import com.villan3ll3.estore.ProductsService.core.data.ProductEntity;
import com.villan3ll3.estore.ProductsService.core.data.ProductsRepository;
import com.villan3ll3.estore.ProductsService.core.events.ProductCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@ProcessingGroup("product-group")
public class ProductEventsHandler {

    private final ProductsRepository repository;

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalStateException exception) {
        // Log error message
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        
        ProductEntity entity = new ProductEntity();
        BeanUtils.copyProperties(event, entity);

        repository.save(entity);
    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent) {

        ProductEntity productEntity = repository.findByProductId(productReservedEvent.getProductId());
        productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
        repository.save(productEntity);

        log.info("ProductReservedEvent is calles for productId: {} and orderId: {}",
        productReservedEvent.getProductId(), productReservedEvent.getOrderId());
    }
}
