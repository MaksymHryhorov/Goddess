package com.java.test.application.repository;

import com.java.test.application.model.Order;
import com.java.test.application.utils.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByUserId(Long userId);
    List<Order> findByOrderDateBeforeAndPaymentStatus(Date date, PaymentStatus paymentStatus);

}