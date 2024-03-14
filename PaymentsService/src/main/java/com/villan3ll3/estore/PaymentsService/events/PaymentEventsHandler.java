package com.villan3ll3.estore.PaymentsService.events;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.villan3ll3.estore.Core.events.PaymentProcessedEvent;
import com.villan3ll3.estore.PaymentsService.data.PaymentEntity;
import com.villan3ll3.estore.PaymentsService.data.PaymentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentEventsHandler {

  private final PaymentRepository paymentRepository;

  public PaymentEventsHandler(PaymentRepository paymentRepository) {
    this.paymentRepository = paymentRepository;
  }

  @EventHandler
  public void on(PaymentProcessedEvent event) {
    log.info("PaymentProcessedEvent is called for orderId: {}", event.getOrderId());

    PaymentEntity paymentEntity = new PaymentEntity();
    BeanUtils.copyProperties(event, paymentEntity);

    paymentRepository.save(paymentEntity);

  }
}
