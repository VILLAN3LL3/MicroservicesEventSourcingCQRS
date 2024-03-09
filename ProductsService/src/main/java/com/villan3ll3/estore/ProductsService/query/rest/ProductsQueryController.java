package com.villan3ll3.estore.ProductsService.query.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.villan3ll3.estore.ProductsService.query.FindProductsQuery;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductsQueryController {

    private final QueryGateway gateway;

    @GetMapping
    public List<ProductRestModel> getProducts() {
        
        FindProductsQuery query = new FindProductsQuery();
        return gateway
            .query(query, ResponseTypes.multipleInstancesOf(ProductRestModel.class))
            .join();
    }
}
