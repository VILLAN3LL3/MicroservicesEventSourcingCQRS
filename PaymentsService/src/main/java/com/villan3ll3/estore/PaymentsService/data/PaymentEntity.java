package com.villan3ll3.estore.PaymentsService.data;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "payments")
public class PaymentEntity implements Serializable {

  @Id
  private String paymentId;

  @Column
  public String orderId;

}