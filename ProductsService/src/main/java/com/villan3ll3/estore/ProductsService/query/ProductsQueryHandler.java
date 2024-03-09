package com.villan3ll3.estore.ProductsService.query;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.villan3ll3.estore.ProductsService.core.data.ProductEntity;
import com.villan3ll3.estore.ProductsService.core.data.ProductsRepository;
import com.villan3ll3.estore.ProductsService.query.rest.ProductRestModel;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ProductsQueryHandler {

    private final ProductsRepository repository;

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductsQuery query) {
        
        List<ProductEntity> storedProducts = repository.findAll();
        List<ProductRestModel> productsRest = new ArrayList<>();
        for(ProductEntity productEntity: storedProducts) {
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(productEntity, productRestModel);
            productsRest.add(productRestModel);
        }
        return productsRest;
    }

}
