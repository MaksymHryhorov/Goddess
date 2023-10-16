package com.java.test.application.service.impl;

import com.java.test.application.model.Order;
import com.java.test.application.model.Product;
import com.java.test.application.model.User;
import com.java.test.application.repository.OrderRepository;
import com.java.test.application.repository.ProductRepository;
import com.java.test.application.repository.UserRepository;
import com.java.test.application.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public String processPayment(final List<Long> productIds, final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        double totalPrice = calculateTotalPrice(productIds); // Calculate the total price of the selected products
        createOrdersForProducts(productIds, user); // Create an order for each selected product
        return "Payment successful. Total amount: " + totalPrice;
    }

    private double calculateTotalPrice(final List<Long> productIds) {
        return productRepository.calculateTotalPrice(productIds);
    }

    private void createOrdersForProducts(final List<Long> productIds, final User user) {
        for (Long productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));
            Order order = new Order();
            order.setUser(user);
            order.setProduct(product);
            order.setQuantity(1); // Assuming a quantity of 1 for each product
            orderRepository.save(order);
        }
    }
}


