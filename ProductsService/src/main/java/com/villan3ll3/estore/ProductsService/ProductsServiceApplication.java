package com.villan3ll3.estore.ProductsService;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

import com.villan3ll3.estore.ProductsService.command.interceptors.CreateProductCommandInterceptor;
import com.villan3ll3.estore.ProductsService.core.errorhandling.ProductsServiceEventsErrorHandler;

@EnableDiscoveryClient
@SpringBootApplication
public class ProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsServiceApplication.class, args);
	}

	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext context, CommandBus commandBus) {

		commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
	}

	@Autowired
	public void configure(EventProcessingConfigurer config) {
		// default error handler:
		// config.registerListenerInvocationErrorHandler("product-group", conf -> PropagatingErrorHandler.instance());

		// custom error handler:
		config.registerListenerInvocationErrorHandler("product-group", conf -> new ProductsServiceEventsErrorHandler());
	}
}
