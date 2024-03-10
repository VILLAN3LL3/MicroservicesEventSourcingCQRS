package com.villan3ll3.estore.OrdersService.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.villan3ll3.estore.OrdersService.core.model.OrderEntity;

public interface OrdersRepository extends JpaRepository<OrderEntity, String> {

    OrderEntity findByOrderId(String orderId);
}
