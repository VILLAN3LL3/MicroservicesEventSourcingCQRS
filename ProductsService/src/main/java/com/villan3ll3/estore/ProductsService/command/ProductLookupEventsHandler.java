package com.villan3ll3.estore.ProductsService.command;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.villan3ll3.estore.ProductsService.core.data.ProductLookupEntity;
import com.villan3ll3.estore.ProductsService.core.data.ProductLookupRepository;
import com.villan3ll3.estore.ProductsService.core.events.ProductCreatedEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {

    private final ProductLookupRepository repository;

    @EventHandler
    public void on(ProductCreatedEvent event) {

        ProductLookupEntity productLookupEntity = new ProductLookupEntity(event.getProductId(), event.getTitle());
        repository.save(productLookupEntity);
    }
}
