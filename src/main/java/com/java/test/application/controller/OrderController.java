package com.java.test.application.controller;

import com.java.test.application.model.Order;
import com.java.test.application.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody final Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable final Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order with ID " + orderId + " has been deleted.");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersForUser(@PathVariable final Long userId) {
        List<Order> userOrders = orderService.getOrdersForUser(userId);
        return ResponseEntity.ok(userOrders);
    }

    @PutMapping("/{orderId}/mark-paid")
    public ResponseEntity<String> markOrderAsPaid(@PathVariable Long orderId) {
        String result = orderService.markOrderAsPaid(orderId);
        return ResponseEntity.ok(result);
    }
}

