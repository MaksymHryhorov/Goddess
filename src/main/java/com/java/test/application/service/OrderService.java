package com.java.test.application.service;

import com.java.test.application.model.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    void deleteOrder(Long orderId);

    List<Order> getOrdersForUser(Long userId);

    String markOrderAsPaid(Long orderId);
}
