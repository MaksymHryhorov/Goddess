package com.java.test.application.service.impl;

import com.java.test.application.model.Order;
import com.java.test.application.model.Product;
import com.java.test.application.model.User;
import com.java.test.application.repository.OrderRepository;
import com.java.test.application.repository.ProductRepository;
import com.java.test.application.repository.UserRepository;
import com.java.test.application.service.OrderService;
import com.java.test.application.utils.PaymentStatus;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Order createOrder(final Order order) {
        User user = getUserById(order.getUser().getId());
        Product product = getProductById(order.getProduct().getId());
        double totalPrice = product.getPrice() * order.getQuantity();
        return orderRepository.save(order);
    }

    public void deleteOrder(final Long orderId) {
        Order order = getOrderById(orderId);
        orderRepository.deleteById(orderId);
    }

    public List<Order> getOrdersForUser(final Long userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

    @Scheduled(fixedRate = 600000) // 10 minutes
    public void deleteUnpaidOrders() {
        Date tenMinutesAgo = new Date(System.currentTimeMillis() - 10 * 60 * 1000); // 10 minutes ago
        List<Order> unpaidOrders = orderRepository
                .findByOrderDateBeforeAndPaymentStatus(tenMinutesAgo, PaymentStatus.PENDING);
        unpaidOrders.forEach(orderRepository::delete);
    }

    public String markOrderAsPaid(final Long orderId) {
        Order order = getOrderById(orderId);
        order.setPaymentStatus(PaymentStatus.PAID);
        orderRepository.save(order);
        return "Order marked as paid.";
    }

    private User getUserById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
    }

    private Product getProductById(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));
    }

    private Order getOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));
    }
}


