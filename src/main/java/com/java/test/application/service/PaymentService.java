package com.java.test.application.service;

import java.util.List;

public interface PaymentService {
    String processPayment(List<Long> productIds, Long userId);
}
