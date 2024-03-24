package com.villan3ll3.estore.OrdersService;

import org.axonframework.config.Configuration;
import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class OrdersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersServiceApplication.class, args);
	}

	@Autowired
	public void configure(EventProcessingConfigurer config) {

		config.registerListenerInvocationErrorHandler("order-group", conf ->
		PropagatingErrorHandler.instance());
	}

  @Bean
  public DeadlineManager deadlineManager(
    Configuration configuration,
    SpringTransactionManager springTransactionManager
  ) {
    return SimpleDeadlineManager
      .builder()
      .scopeAwareProvider(new ConfigurationScopeAwareProvider(configuration))
      .transactionManager(springTransactionManager)
      .build();
  }
}
