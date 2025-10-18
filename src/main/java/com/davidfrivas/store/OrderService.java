package com.davidfrivas.store;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service // Manage objects of type OrderService
public class OrderService {
    // Private field to store payment service
    private PaymentService paymentService;

    // Constructor injection
    // Pass our dependency as an argument to this constructor
    // Use PayPal payment service
    public OrderService(@Qualifier("paypal") PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder() {
        paymentService.processPayment(10);
    }

    // Setter injection: setter method for dependency injection
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
