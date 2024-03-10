package com.villan3ll3.estore.ProductsService.command.interceptors;

import java.util.List;
import java.util.function.BiFunction;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.villan3ll3.estore.ProductsService.command.CreateProductCommand;
import com.villan3ll3.estore.ProductsService.core.data.ProductLookupEntity;
import com.villan3ll3.estore.ProductsService.core.data.ProductLookupRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);
    private final ProductLookupRepository repository;

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            List<? extends CommandMessage<?>> messages) {

        return (index, command) -> {

            LOGGER.info("Intercepted command: " + command.getPayloadType());

            if (CreateProductCommand.class.equals(command.getPayloadType())) {

                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
                ProductLookupEntity productLookupEntity = repository
                    .findByProductIdOrTitle(createProductCommand.getProductId(), createProductCommand.getTitle());

                if(productLookupEntity != null) {
                    throw new IllegalStateException(
                        String.format("Product with productId %s or title %s already exists",
                        createProductCommand.getProductId(), createProductCommand.getTitle()));
                }
            }
            return command;
        };
    }
}
